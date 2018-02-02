package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

public class VariableExists extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__exist_variable";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	//private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("Name of variable (may include variable and function references)");
	}
	/**
	 * No-arg constructor.
	 */
	public VariableExists() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String variableName = new String(((CompoundVariable) values[0]).execute().trim());
		JMeterVariables variables = getVariables();
		Set<java.util.Map.Entry<String, Object>> vEntries = variables.entrySet();
		try {
			for (java.util.Map.Entry<String, Object> entry : vEntries) {
				String key = entry.getKey().toString();
				if (variableName.equals(key)) {
					return "true";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "false";
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
