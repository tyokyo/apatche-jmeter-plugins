package org.apache.jmeter.sampler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.sampler.util.ApiTool;
import org.apache.jmeter.sampler.util.JsonParse;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.samplers.Entry;

import sioeye.spider.helpers.PropertyHelpers;

public class ApiSampler extends AbstractSampler {
	/** 
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */ 
	private static final long serialVersionUID = 1L;
	public final static String SERVER = "服务器名称:"; 
	public final static String METHOD = "路径:";
	public final static String ARGS = "参数列表(逗号分隔):";
	public final static String DESCRIPTION = "描述";
	private static final String PROPSTATUS= "ApiSampler.prop.status";
	private static final String VARSSTATUS = "ApiSampler,var.status";
	private static final String VARIABLES_NAMES= "ApiSampler.var.adds";
	private static Logger log = LoggerFactory.getLogger(ApiSampler.class);
	private static final String USER_DEFINED_VARIABLES = "ApiSampler.user_defined_variables"; //$NON-NLS-1$

	public void setStoredVariables(String varsString){
		setProperty(VARIABLES_NAMES, varsString);
	}
	public String getStoredVariables(){
		return getPropertyAsString(VARIABLES_NAMES);
	}
	public boolean  getProps() {
		return getPropertyAsBoolean(PROPSTATUS);
	}
	public void setProps(Boolean status) {
		setProperty(PROPSTATUS, status);
	}
	public boolean  getVars() {
		return getPropertyAsBoolean(VARSSTATUS);
	}
	public void setVars(Boolean status) {
		setProperty(VARSSTATUS, status);
	}
	public  String storeResponseVaribles(JMeterContext threadContext,String json,String keyString){
		log.warn(json);
		StringBuffer buffer=new StringBuffer();
		String[] keys = keyString.trim().split(",");
		if (getVars()) {
			buffer.append("\nJMeterVariables\n");
		}else {
			buffer.append("\nJMeterProperties\n");
		}
		for (String key : keys) {
			String value=null;
			try {
				JsonParse js = new JsonParse();
				js.setJsonString(json);
				js.setQueryString("."+key);
				List<Object> skens = js.parse();
				if (skens.size()==1) {
					value =skens.get(0).toString();
				}
				js.print();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (value!=null) {
				if (getVars()) {
					JMeterVariables resVariables = threadContext.getVariables();
					resVariables.put(key, value);
					buffer.append(String.format("%s=%s\n", key,value));
				}else {
					JMeterUtils.getJMeterProperties().put(key, value);
					buffer.append(String.format("%s=%s\n", key,value));
				}
			}
		}
		return buffer.toString();
	}
	@Override
	public SampleResult sample(Entry arg0) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SampleResult res = new SampleResult();
		res.sampleStart();
		String url=String.format("https://%s%s", getServer(),getMethod());
	    Map<String, String> header = new HashMap<String, String>();
	    //if vars
		JMeterContext threadContext = getThreadContext();
		JMeterVariables variables = threadContext.getVariables();
		Set<java.util.Map.Entry<String, Object>> vEntries = variables.entrySet();
		try {
			for (java.util.Map.Entry<String, Object> entry : vEntries) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				if ("sessiontoken".equals(key)) {
					header.put("X_sioeye_sessiontoken", value);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		HashMap<String, String> headerMap = PropertyHelpers.getHeaderMap();
		Set<String> keys = headerMap.keySet();
		for (String key : keys) {
			String value = headerMap.get(key);
			header.put(key, value);
		}
		//if props
		try {
			String X_sioeye_sessiontoken =JMeterUtils.getJMeterProperties().get("sessiontoken").toString();
			if (X_sioeye_sessiontoken!=null) {
				header.put("X_sioeye_sessiontoken", X_sioeye_sessiontoken);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		ApiTool.post(res,url, header, getUserDefinedVariables());
		
	/*	JsonTool.hasElement(bean, "success", "false");
		JsonTool.hasElement(bean, "code", "3007");
		JsonTool.hasElement(bean, "code", "3008");*/
		/*JsonTool.hasElement(bean, "success:false,code:3007");
		JsonTool.hasElement(bean, "success:false,code:3008");
		JsonTool.hasElement(bean, "success:false");*/
		
	    //resonseData=bean.getResponsehtml();
		res.setSampleLabel(getName());
		//res.setResponseData("setResponseData", null);
		res.setDataType(SampleResult.TEXT);
		
		String varLogsString = storeResponseVaribles(getThreadContext(), new String(res.getResponseData()),getStoredVariables());
		res.setResponseData(new String(res.getResponseData())+varLogsString,null);
		res.setSamplerData(ApiTool.getSamplerData(url, header, getUserDefinedVariables()));
		res.setResponseOK();
		res.sampleEnd();
		return res;
	}
	public void setUserDefinedVariables(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_VARIABLES, vars));
	}

	public JMeterProperty getUserDefinedVariablesAsProperty() {
		return getProperty(USER_DEFINED_VARIABLES);
	}
	public Map<String, String> getUserDefinedVariables() {
		Arguments args = getVariables();
		return args.getArgumentsAsMap();
	}
	private Arguments getVariables() {
		Arguments args = (Arguments) getProperty(USER_DEFINED_VARIABLES).getObjectValue();
		if (args == null) {
			args = new Arguments();
			setUserDefinedVariables(args);
		}
		return args;
	}
	public void setServer(String serverUrl){
		setProperty(SERVER, serverUrl);
	}
	public String getServer(){
		return getPropertyAsString(SERVER);
	}
	public void setDescription(String description){
		setProperty(DESCRIPTION, description);
	}
	public String getDescription(){
		return getPropertyAsString(DESCRIPTION);
	}
	public void setMethod(String method){
		setProperty(METHOD, method);
	}
	public String getMethod(){
		return getPropertyAsString(METHOD);
	}
}