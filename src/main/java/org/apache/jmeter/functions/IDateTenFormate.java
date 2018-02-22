package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

public class IDateTenFormate extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__scheduledend";
	private static final int MAX_PARAM_COUNT = 2;
	private static final int MIN_PARAM_COUNT = 2;
	//private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("The standard time 10 timestamp-scheduledstart,null means there is no start time");
		desc.add("The time(hour) for  room,null  means there is no end time");
	}
	public static String scheduledendTime(String scheduledstart,String duration )
	{
		try {
			long timeStampStart = Long.parseLong(scheduledstart);
			double hours = Double.parseDouble(duration);
			long room=(long)(60*60*hours);
			long timeStampSec = timeStampStart+room;
			String result = String.format("%010d", timeStampSec);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	/**
	 * No-arg constructor.
	 */
	public IDateTenFormate() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String scheduledstart=new String(((CompoundVariable) values[0]).execute().trim());
		String duration = new String(((CompoundVariable) values[1]).execute().trim());
		if ("null".toUpperCase().equals(scheduledstart.toUpperCase())) {
			scheduledstart=System.currentTimeMillis()/1000+"";
			
		}
		if ("null".toUpperCase().equals(duration.toUpperCase())) {
			return "";
		}
		String scheduledend = scheduledendTime(scheduledstart,duration);
		return scheduledend;
	}

	/** {@inheritDoc} */
	@Override
	public String getReferenceKey() {
		return KEY;
	}

	@Override
	public List<String> getArgumentDesc() {
		return desc;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		checkParameterCount(parameters, MIN_PARAM_COUNT, MAX_PARAM_COUNT);
		values = parameters.toArray();
	}
}
