package org.apache.jmeter.sampler;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
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

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import sioeye.spider.helpers.PropertyHelpers;

public class ApiSampler extends AbstractSampler {
	private static final long serialVersionUID = 1L;
	public final static String SERVER = "服务器名称:"; 
	public final static String METHOD = "路径:";
	public final static String ARGS = "参数列表(逗号分隔):";
	public final static String DESCRIPTION = "描述";
	private static final String VAR_SESSIONTOKEN= "ApiSampler.var.sessiontoken";
	private static final String PROPSTATUS= "ApiSampler.prop.status";
	private static final String VARSSTATUS = "ApiSampler,var.status";
	private static final String VARIABLES_NAMES= "ApiSampler.var.adds";
	private static Logger log = LoggerFactory.getLogger(ApiSampler.class);
	private static final String USER_DEFINED_VARIABLES = "ApiSampler.user_defined_variables"; 
	private static final String USER_DEFINED_HEADERS = "ApiSampler.user_defined_headers"; 
	public static final DecimalFormat decimalFormatter = new DecimalFormat("#.#");
	static {
		decimalFormatter.setMaximumFractionDigits(340); 
		decimalFormatter.setMinimumFractionDigits(1);
	}

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
	public boolean  getSessionToken() {
		return getPropertyAsBoolean(VAR_SESSIONTOKEN);
	}
	public void setSessionToken(Boolean status) {
		setProperty(VAR_SESSIONTOKEN, status);
	}
	public static String objectToString(Object subj) {
		String str;
		if (subj == null) {
			str = "null";
		} else if (subj instanceof Map) {
			str = new JSONObject((Map<String, ?>) subj).toJSONString();
		} else if (subj instanceof Double || subj instanceof Float) {
			str = decimalFormatter.format(subj);
		} else {
			str = subj.toString();
		}
		return str;
	}
	public  String storeResponseVaribles(JMeterContext threadContext,String responseData,String keyString ,String defaultValue){
		JMeterVariables vars = threadContext.getVariables();
		StringBuffer buffer=new StringBuffer();
		String[] keys = keyString.trim().split(",");
		if (getVars()) {
			for (String key : keys) {
				String keyStore="";
				// TODO Auto-generated method stub
				if (key.contains("=")) {
					String[] kkey = key.split("=");
					if (kkey.length==2) {
						if (!"".equals(kkey[0])) {
							key=kkey[0];
							keyStore=kkey[1];
						}
					}else {
						key="";
					}
				}else {
					keyStore=key;
				}
				String jsonQueryString="."+key;
				try {
					Object jsonPathResult = JsonPath.read(responseData, jsonQueryString);
					Object[] arr = ((JSONArray) jsonPathResult).toArray();
					if (arr.length==0) {
						log.info(key+":Query array is empty");
					}else if (arr.length==1) {
						log.info("putVariables:key="+keyStore+" value="+objectToString(arr[0]));
						vars.put(keyStore, objectToString(arr[0]));
					}else {
						vars.put(keyStore, objectToString(jsonPathResult));
						log.info("putVariables:key="+keyStore+" value="+objectToString(jsonPathResult));

						vars.put(keyStore+ "_matchNr", objectToString(arr.length));
						log.info("putVariables:key="+keyStore+ "_matchNr"+" value="+objectToString(arr.length));

						int k = 1;
						while (vars.get(keyStore + "_" + k) != null) {
							vars.remove(keyStore + "_" + k);
							k++;
						}
						for (int n = 0; n < arr.length; n++) {
							vars.put(keyStore+ "_" + (n + 1), objectToString(arr[n]));
							log.info("putVariables:key="+keyStore+ "_" + (n + 1)+" value="+objectToString(arr[n]));
						}
					}
				} catch (Exception e) {
					log.debug("Query failed", e);
					vars.put(keyStore, defaultValue);
					vars.put(keyStore+ "_matchNr", "0");
					int k = 1;
					while (vars.get(keyStore + "_" + k) != null) {
						vars.remove(keyStore + "_" + k);
						k++;
					}
				}
			}
		}else {
			for (String key : keys) {
				String jsonQueryString="."+key;
				Object jsonPathResult = JsonPath.read(responseData, jsonQueryString);
				JMeterUtils.getJMeterProperties().put(key, objectToString(jsonPathResult));
			}
		}
		//do sessiontoken 
		String sessiontokenKey=".sessiontoken";
		Object sessionKeyResult = JsonPath.read(responseData, sessiontokenKey);
		Object[] arr = ((JSONArray) sessionKeyResult).toArray();
		if (arr.length==0) {
			log.info(sessiontokenKey+":Query array is empty");
		}else if (arr.length==1) {
			String value = objectToString(arr[0]);
			log.info("jsonQueryString="+sessiontokenKey+" value="+value);
			getThreadContext().getVariables().put("sessiontoken",value );
		}
		return buffer.toString();
	}
	public  String storeResponseVaribles(JMeterContext threadContext,String json,String keyString){
		//log.warn(json);
		StringBuffer buffer=new StringBuffer();
		String[] keys = keyString.trim().split(",");
		if (getVars()) {
			buffer.append("\nJMeterVariables\n");
		}else {
			buffer.append("\nJMeterProperties\n");
		}
		HashMap<String, String> kvs = new HashMap<String, String>();
		for (String key : keys) {
			String value=null;
			try {
				JsonParse js = new JsonParse();
				js.setJsonString(json);
				js.setQueryString("."+key);
				List<Object> skens = js.parse();

				if (skens.size()==1) {
					value =skens.get(0).toString();
					kvs.put(key+"_matchNr", skens.size()+"");
					kvs.put(key, value);
				}else {
					kvs.put(key+"_matchNr", skens.size()+"");
					for (int i = 0; i <skens.size(); i++) {
						if (i==0) {
							value =skens.get(i).toString();
							kvs.put(key, value);
						}else {
							value =skens.get(i).toString();
							kvs.put(key+"_"+i, value);
						}
					}
				}
				js.print();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (kvs!=null) {
			if (getVars()) {
				JMeterVariables resVariables = threadContext.getVariables();
				for (String key : kvs.keySet()) {
					String value = kvs.get(key);
					resVariables.put(key, value);
					buffer.append(String.format("%s=%s\n", key,value));
				}
			}else {
				for (String key : kvs.keySet()) {
					String value = kvs.get(key);
					JMeterUtils.getJMeterProperties().put(key, value);
					buffer.append(String.format("%s=%s\n", key,value));
				}
			}
		}
		log.info(buffer.toString());
		return buffer.toString();
	}
	@Override
	public HTTPSampleResult sample(Entry arg0) {
		HTTPSampleResult res = new HTTPSampleResult();
		res.sampleStart();
		String url=String.format("https://%s%s", getServer(),getMethod());
		res.setHTTPMethod("POST");
		Map<String, String> headers=getUserDefinedHeaders();

		if (getSessionToken()) {
			JMeterContext threadContext = getThreadContext();
			JMeterVariables variables = threadContext.getVariables();
			Set<java.util.Map.Entry<String, Object>> vEntries = variables.entrySet();
			try {
				for (java.util.Map.Entry<String, Object> entry : vEntries) {
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					if ("sessiontoken".equals(key)) {
						headers.put("X_sioeye_sessiontoken", value);
						break;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			res.setURL(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ApiTool.post(res,url, getUserDefinedHeaders(), getUserDefinedVariables());
		Map<String, String> paramsMap=getUserDefinedVariables();
		res.setQueryString(ApiTool.queryString(paramsMap).replace("{", "{\n").replace("}", "}\n").replace(",", ",\n"));
		ApiTool.post(res,url, headers, paramsMap);
		
		//resonseData=bean.getResponsehtml();
		res.setSampleLabel(getName());
		//res.setResponseData("setResponseData", null);
		res.setDataType(SampleResult.TEXT);
		//String varLogsString = storeResponseVaribles(getThreadContext(), new String(res.getResponseData()),getStoredVariables());
		String varLogsString = storeResponseVaribles(getThreadContext(), new String(res.getResponseData()),getStoredVariables(),"");
		log.info(varLogsString);
		//res.setResponseData(new String(res.getResponseData())+varLogsString,null);
		res.setResponseData(new String(res.getResponseData()),null);
		res.setRequestHeaders(ApiTool.getHeaderStrings(headers)+"\n[Thread Group Variables] \n"+getVariables(getThreadContext()));
		//res.setSamplerData(ApiTool.getSamplerData(url, headers, getUserDefinedVariables())+"\n"+varLogsString+"");
		res.setResponseOK();
		
		res.sampleEnd();
		return res;
	}
	public static String getVariables(JMeterContext threadContext){
		JMeterVariables variables = threadContext.getVariables();
		StringBuffer varBuffer=new StringBuffer();
		Set<java.util.Map.Entry<String, Object>> vEntries = variables.entrySet();
		try {
			for (java.util.Map.Entry<String, Object> entry : vEntries) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				String line = String.format("%s:%s", key,value);
				varBuffer.append(line+"\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return varBuffer.toString();
	}
	public void setUserDefinedVariables(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_VARIABLES, vars));
	}
	public JMeterProperty getUserDefinedVariablesAsProperty() {
		return getProperty(USER_DEFINED_VARIABLES);
	}
	public void setUserDefinedHeaders(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_HEADERS, vars));
	}
	public JMeterProperty getUserDefinedHeadersAsProperty() {
		return getProperty(USER_DEFINED_HEADERS);
	}
	public Map<String, String> getUserDefinedVariables() {
		Arguments args = getVariables();
		return args.getArgumentsAsMap();
	}
	public Map<String, String> getUserDefinedHeaders() {
		Arguments args = getHeaders();
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
	private Arguments getHeaders() {
		Arguments args = (Arguments) getProperty(USER_DEFINED_HEADERS).getObjectValue();
		if (args == null) {
			args = new Arguments();
			setUserDefinedHeaders(args);
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