package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

public class DateTenFormate extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__scheduledstart";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	//private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("scheduledstart time after current time(/hour)");
	}
	public String timeStamp(double duration)
	{
		long time = (long)(60*60*duration);
		long timeStampSec = System.currentTimeMillis()/1000+time;
		String timestamp = String.format("%010d", timeStampSec);
		return timestamp;
	}
	/**
	 * No-arg constructor.
	 */
	public DateTenFormate() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String start = new String(((CompoundVariable) values[0]).execute().trim());
		if ("null".equals(start)) {
			return "";
		}
		try {
			double duration = Double.parseDouble(start);
			return  timeStamp(duration);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
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
