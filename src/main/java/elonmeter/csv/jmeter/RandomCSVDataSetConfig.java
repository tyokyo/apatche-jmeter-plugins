package elonmeter.csv.jmeter;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import elonmeter.csv.RandomCSVReader;

import java.io.Serializable;
import java.util.ArrayList;

import kg.apc.jmeter.JMeterPluginsUtils;

public class RandomCSVDataSetConfig extends ConfigTestElement implements NoThreadClone, LoopIterationListener, TestStateListener {
	private static final Logger log = LoggingManager.getLoggerForClass();
	public static final String VARIABLE_NAMES = "variableNames";
	public static final String RANDOM_ORDER = "randomOrder";
	public static final String REWIND_ON_THE_END = "rewindOnTheEndOfList";
	public static final String INDEPENDENT_LIST_PER_THREAD = "independentListPerThread";
	private int curPos = 0;
	private final ThreadLocal<RandomCSVReader> threadLocalRandomCSVReader = new ThreadLocalSerializable<RandomCSVReader>() {
		@Override
		protected RandomCSVReader initialValue() {
			return createRandomCSVReader();
		}
	};
	public JMeterProperty getData() {
		JMeterProperty brokenProp = getProperty("threads_schedule");
		JMeterProperty usualProp = getProperty("ultimatethreadgroupdata");

		if ((brokenProp instanceof CollectionProperty)) {
			if ((usualProp == null) || ((usualProp instanceof NullProperty))) {
				log.warn("Copying 'threads_schedule' into 'ultimatethreadgroupdata'");
				JMeterProperty newProp = brokenProp.clone();
				newProp.setName("ultimatethreadgroupdata");
				setProperty(newProp);
			}
			log.warn("Removing property 'threads_schedule' as invalid");
			removeProperty("threads_schedule");
		}

		CollectionProperty overrideProp = getLoadFromExternalProperty();
		if (overrideProp != null) {
			return overrideProp;
		}

		return getProperty("ultimatethreadgroupdata");
	}
	private CollectionProperty getLoadFromExternalProperty()
	{
		String loadProp = JMeterUtils.getProperty("threads_schedule");
		log.debug("Profile prop: " + loadProp);
		if ((loadProp != null) && (loadProp.length() > 0))
		{
			log.info("GUI threads profile will be ignored");
			String[] headers=getVariableNames().split(",");
			int hsize=headers.length;
			Class[] str = new Class[hsize];
			for (int i = 0; i < headers.length; i++) {
				str[i]=String.class;
			}
			
			PowerTableModel dataModel = new PowerTableModel(headers, str);
			String[] chunks = loadProp.split("\\)");

			for (String chunk : chunks) {
				try {
					parseChunk(chunk, dataModel);
				} catch (RuntimeException e) {
					log.warn("Wrong  chunk ignored: " + chunk, e);
				}
			}

			log.info("Setting threads profile from property threads_schedule: " + loadProp);
			return JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, "ultimatethreadgroupdata");
		}
		return null;
	}
	private static void parseChunk(String chunk, PowerTableModel model) {
		log.debug("Parsing chunk: " + chunk);
		String[] parts = chunk.split("[(,]");
		String loadVar = parts[0].trim();

		if (loadVar.equalsIgnoreCase("spawn")) {
			Integer[] row = new Integer[5];
			row[0] = Integer.valueOf(Integer.parseInt(parts[1].trim()));
			row[1] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[2]));
			row[2] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[3]));
			row[3] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[4]));
			row[4] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[5]));
			model.addRow(row);
		} else {
			throw new RuntimeException("Unknown load type: " + parts[0]);
		}
	}
	private static class ThreadLocalSerializable<T> extends ThreadLocal<T> implements Serializable {
	}

	private RandomCSVReader randomCSVReader;

	@Override
	public void iterationStart(LoopIterationEvent loopIterationEvent) {
		readTableVariables();
	}

	private void readRandom() {
		final RandomCSVReader reader = getReader();
		long lineAddr;
		synchronized (reader) {
			if (reader.hasNextRecord()) {
				lineAddr = reader.getNextLineAddr();
			} else {
				// TODO: interrupt iteration
				randomCSVReader = null;
				throw new JMeterStopThreadException("All records in the CSV file have been passed.");
			}
		}

		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		putVariables(variables, getDestinationVariableKeys(), reader.readLineWithSeek(lineAddr));
	}


	private void readConsistent() {
		final RandomCSVReader reader = getReader();
		synchronized (reader) {
			if (reader.hasNextRecord()) {
				JMeterVariables variables = JMeterContextService.getContext().getVariables();
				putVariables(variables, getDestinationVariableKeys(), reader.readNextLine());
			} else {
				// TODO: interrupt iteration
				randomCSVReader = null;
				throw new JMeterStopThreadException("All records in the CSV file have been passed.");
			}
		}
	}
	private void readTableVariables() {
		JMeterProperty threadValues=getData();
		CollectionProperty prop = (CollectionProperty)threadValues;
		int rowC=prop.size();
		if (rowC==0) {
			return;
		}
		if (curPos==rowC) {
			return;
		}
		try {
			@SuppressWarnings("unchecked")
			JMeterProperty jMeterProperty =prop.get(curPos);
			ArrayList<String> rowObject = (ArrayList<String>) prop.get(curPos).getObjectValue();
			int rowSize=rowObject.size();
			log.info(rowSize+"-"+rowObject.toString());
			String data=rowObject.toString().replace("[", "");
			data=data.replace("]", "");
			String[] values=data.split(",");
			JMeterVariables variables = JMeterContextService.getContext().getVariables();
			putVariables(variables, getDestinationVariableKeys(), values);
			
			curPos=curPos+1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new JMeterStopThreadException(curPos+"-"+rowC+"All records in the table have been passed.");
		}
	}
	

	public String[] getDestinationVariableKeys() {
		String vars = getVariableNames();
		return hasVariablesNames() ?
				JOrphanUtils.split(vars, ",") :
					getReader().getHeader();
	}

	private void putVariables(JMeterVariables variables, String[] keys, String[] values) {
		int minLen = (keys.length > values.length) ? values.length : keys.length;
		for (int i = 0; i < minLen; i++) {
			variables.put(keys[i].trim(), values[i].trim());
			log.warn("putVariables-key="+keys[i]+" value="+values[i]);
		}
	}

	private RandomCSVReader getReader() {
		return isIndependentListPerThread() ? threadLocalRandomCSVReader.get() : randomCSVReader;
	}

	private RandomCSVReader createRandomCSVReader() {
		return new RandomCSVReader(
				isRandomOrder(),
				hasVariablesNames(),
				isRewindOnTheEndOfList()
				);
	}

	private boolean hasVariablesNames() {
		String vars = getVariableNames();
		return (vars != null && !vars.isEmpty());
	}

	@Override
	public void testStarted() {
		testStarted("*local*");
	}

	@Override
	public void testStarted(String s) {
		randomCSVReader = createRandomCSVReader();
	}

	@Override
	public void testEnded() {
		testEnded("*local*");
	}

	@Override
	public void testEnded(String s) {
		randomCSVReader = null;
	}
	public String getVariableNames() {
		return getPropertyAsString(VARIABLE_NAMES);
	}

	public void setVariableNames(String variableNames) {
		setProperty(VARIABLE_NAMES, variableNames);
	}

	public boolean isRandomOrder() {
		return getPropertyAsBoolean(RANDOM_ORDER);
	}

	public void setRandomOrder(boolean randomOrder) {
		setProperty(RANDOM_ORDER, randomOrder);
	}
	
	public boolean isRewindOnTheEndOfList() {
		return getPropertyAsBoolean(REWIND_ON_THE_END);
	}

	public void setRewindOnTheEndOfList(boolean rewindOnTheEndOfList) {
		setProperty(REWIND_ON_THE_END, rewindOnTheEndOfList);
	}

	public boolean isIndependentListPerThread() {
		return getPropertyAsBoolean(INDEPENDENT_LIST_PER_THREAD);
	}

	public void setIndependentListPerThread(boolean independentListPerThread) {
		setProperty(INDEPENDENT_LIST_PER_THREAD, independentListPerThread);
	}
	public void setData(CollectionProperty rows)
	{
		setProperty(rows);
	}
}
