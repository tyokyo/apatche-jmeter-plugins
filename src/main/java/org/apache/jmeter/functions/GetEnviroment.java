package org.apache.jmeter.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GetEnviroment extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__getEnv";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("get System enviroment");
	}
	/**
	 * No-arg constructor.
	 */
	public GetEnviroment() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String envString = new String(((CompoundVariable) values[0]).execute().trim());
		String envValueString="";
		Map<String, String> map = System.getenv();
		for(Iterator<String> itr = map.keySet().iterator();itr.hasNext();){
			String key = itr.next();
			if (envString.equals(key)) {
				envValueString=map.get(key);
				log.info(key + "=" + map.get(key));
			}
		}  
		return envValueString;
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

