package org.apache.jmeter.functions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class GetConfiguration extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__getConfiguration";
	private static final int MAX_PARAM_COUNT = 2;
	private static final int MIN_PARAM_COUNT = 2;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("property file path");
		desc.add("key in property file");
	}
	/**
	 * No-arg constructor.
	 */
	public GetConfiguration() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String path = new String(((CompoundVariable) values[0]).execute().trim());
		String _s_key = new String(((CompoundVariable) values[1]).execute().trim());
		String _s_value="";
		Properties prop = new Properties();     
		//读取属性文件a.properties
		try {
			InputStream  in = new FileInputStream(path);
			prop.load(in);     ///加载属性列表
			Iterator<String> it=prop.stringPropertyNames().iterator();
			while(it.hasNext()){
				String _p_key=it.next();
				if (_s_key.equals(_p_key)) {
					_s_value=prop.getProperty(_p_key);
					log.info(_p_key+":"+prop.getProperty(_p_key));
					break;
				}
			}
			in.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _s_value;
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

