package org.apache.backup;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

public class Variables extends AbstractFunction {
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__vars.get";
    private static final int MAX_PARAM_COUNT = 1;
    private static final int MIN_PARAM_COUNT = 1;
    private Object[] values;

    static {
        desc.add("vars.get");
    }

    /**
     * No-arg constructor.
     */
    public Variables() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
    	String key = new String(((CompoundVariable) values[0]).execute().trim());
    	JMeterVariables jMeterVariables=getVariables();
    	String value="";
    	try {
    		value = jMeterVariables.get(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
        return value==null?"":value;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters, MIN_PARAM_COUNT, MAX_PARAM_COUNT);
        values = parameters.toArray();
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
}

