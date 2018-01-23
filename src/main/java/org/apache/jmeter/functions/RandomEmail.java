package org.apache.jmeter.functions;

import groovy.util.logging.Log;

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

public class RandomEmail extends AbstractFunction {
	private static final String[] email_suffix="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__RandomEmail";
	private static final int MAX_PARAM_COUNT = 2;
	private static final int MIN_PARAM_COUNT = 2;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("get random email min length");
		desc.add("get random email max length");
	}
	   private static int getNum(int start, int end) {
	        return (int)(Math.random()*(end-start+1)+start);
	    }
	 /**
     * 返回Email
     * @param lMin 最小长度
     * @param lMax 最大长度
     * @return
     */
    public static String getRandomEmail(int lMin,int lMax) {
        int length=getNum(lMin,lMax);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int)(Math.random()*base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int)(Math.random()*email_suffix.length)]);
        return sb.toString();
    }
	/**
	 * No-arg constructor.
	 */
	public RandomEmail() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String minString = new String(((CompoundVariable) values[0]).execute().trim());
		String maxString = new String(((CompoundVariable) values[1]).execute().trim());
		String email = getRandomEmail(Integer.parseInt(minString), Integer.parseInt(maxString));
		log.info("email-"+email);
		return email;
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
