package ckt.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

public class Pic2Base64 extends AbstractFunction {
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__Pic2Base64";
    private static final int MAX_PARAM_COUNT = 1;
    private static final int MIN_PARAM_COUNT = 1;
    private Object[] values;

    static {
        desc.add("picture to base64");
    }

    /**
     * No-arg constructor.
     */
    public Pic2Base64() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
            throws InvalidVariableException {
    	Util util = new Util();
    	//String baseString = util.pic2Base64("C:/Users/admin/Desktop/ycb2.jpg");
    	//参数传递
    	String picture = new String(((CompoundVariable) values[0]).execute().trim());
    	String baseString = util.pic2Base64(picture);
        return baseString;
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
