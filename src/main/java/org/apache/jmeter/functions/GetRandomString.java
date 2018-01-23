package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GetRandomString extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__GetRandomString";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("random String length");
	}
	public static String getRandomString(int length){  
		Random random = new Random();  
		StringBuffer sb = new StringBuffer();  
		for(int i = 0; i < length; ++i){  
			int number = random.nextInt(3);  
			long result = 0;  
			switch(number){  
			case 0:  
				result = Math.round(Math.random() * 25 + 65);  
				sb.append(String.valueOf((char)result));  
				break;  
			case 1:  
				result = Math.round(Math.random() * 25 + 97);  
				sb.append(String.valueOf((char)result));  
				break;  
			case 2:  
				sb.append(String.valueOf(new Random().nextInt(10)));  
				break;  
			}  
		}  
		return sb.toString();     
	}  

	/**
	 * No-arg constructor.
	 */
	public GetRandomString() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String value = new String(((CompoundVariable) values[0]).execute().trim());
		int length =Integer.parseInt(value);
		String randomString=getRandomString(length);
		log.info("generate random string-"+randomString);
		return randomString;
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
