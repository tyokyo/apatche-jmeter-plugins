package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class RandomTelephone extends AbstractFunction {
	/**
	 * 返回手机号码
	 */
	private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__RandomPhoneNumber";
	private static final int MAX_PARAM_COUNT = 0;
	private static final int MIN_PARAM_COUNT = 0;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		//desc.add("Convert String to MD5");
	}

	/**
	 * No-arg constructor.
	 */
	public RandomTelephone() {
		super();
	}
	private static int getNum(int start, int end) {
		return (int)(Math.random()*(end-start+1)+start);
	}
	public static String getRandomTel() {
		int index=getNum(0,telFirst.length-1);
		String first=telFirst[index];
		String second=String.valueOf(getNum(1,888)+10000).substring(1);
		String thrid=String.valueOf(getNum(1,9100)+10000).substring(1);
		return first+second+thrid;
	}
	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String numString = getRandomTel();
		log.info("telephone-number:"+numString);
		return numString;
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
