package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.sampler.util.IdCardGenerator;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GetUUID extends AbstractFunction {
	/**
	 * 返回手机号码
	 */
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__GetUUID";
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
	public GetUUID() {
		super();
	}
	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		IdCardGenerator g = new IdCardGenerator();
		String uuid = g.generate();
		log.info("uuid-number:"+uuid);
		return uuid;
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
