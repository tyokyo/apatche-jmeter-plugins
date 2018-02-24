package org.apache.jmeter.sampler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import kg.apc.jmeter.JMeterPluginsUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.sampler.util.ApiTool;
import org.apache.jmeter.sampler.util.HttpsPostFile;
import org.apache.jmeter.sampler.util.HttpsPostText;
import org.apache.jmeter.sampler.util.JsonParse;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.samplers.Entry;

import com.jayway.jsonpath.JsonPath;

public class ApiSampler extends AbstractSampler {
	private static final long serialVersionUID = 1L;
	private static final Pattern PORT_PATTERN = Pattern.compile("\\d+");
	public static  String[] columnIdentifiers = {"文件名称","参数名称","MIME类型"};
	@SuppressWarnings("rawtypes")
	public static  Class[] columnClasses = {String.class,String.class,String.class};
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
	private static final String USER_DEFINED_FILES_UPLOAD = "ApiSampler.user_defined_files"; 
	public static final DecimalFormat decimalFormatter = new DecimalFormat("#.#");
	public JMeterProperty getData() {
		JMeterProperty brokenProp = getProperty("threads_schedule");
		JMeterProperty usualProp = getProperty("threads_file_upload");

		if ((brokenProp instanceof CollectionProperty)) {
			if ((usualProp == null) || ((usualProp instanceof NullProperty))) {
				log.warn("Copying 'threads_schedule' into 'threads_file_upload'");
				JMeterProperty newProp = brokenProp.clone();
				newProp.setName("threads_file_upload");
				setProperty(newProp);
			}
			log.warn("Removing property 'threads_schedule' as invalid");
			removeProperty("threads_schedule");
		}

		CollectionProperty overrideProp = getLoadFromExternalProperty();
		if (overrideProp != null) {
			return overrideProp;
		}
		return getProperty("threads_file_upload");
	}
	private CollectionProperty getLoadFromExternalProperty()
	{
		String loadProp = JMeterUtils.getProperty("threads_schedule");
		log.debug("Profile prop: " + loadProp);
		if ((loadProp != null) && (loadProp.length() > 0))
		{
			PowerTableModel dataModel = new PowerTableModel(columnIdentifiers, columnClasses);
			String[] chunks = loadProp.split("\\)");
			for (String chunk : chunks) {
				try {
					parseChunk(chunk, dataModel);
				} catch (RuntimeException e) {
					log.warn("Wrong  chunk ignored: " + chunk, e);
				}
			}
			log.info("Setting threads profile from property threads_schedule: " + loadProp);
			return JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, "threads_file_upload");
		}
		return null;
	}
	private static void parseChunk(String chunk, PowerTableModel model) {
		log.debug("Parsing chunk: " + chunk);
		String[] parts = chunk.split("[(,]");
		String loadVar = parts[0].trim();

		if (loadVar.equalsIgnoreCase("spawn")) {
			Integer[] row = new Integer[5];
			row[0] = Integer.valueOf(Integer.parseInt(parts[1].trim()));
			row[1] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[2]));
			row[2] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[3]));
			row[3] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[4]));
			row[4] = Integer.valueOf(JMeterPluginsUtils.getSecondsForShortString(parts[5]));
			model.addRow(row);
		} else {
			throw new RuntimeException("Unknown load type: " + parts[0]);
		}
	}
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
	public CollectionProperty getHeaderManager() {
		CollectionProperty columns=null;
		JMeterProperty headerProperty = getProperty("HeaderManager.headers");
		if (!(headerProperty instanceof NullProperty)) {
			columns = (CollectionProperty)headerProperty;
		}
		return columns;
	}
	protected void setConnectionHeaders(Map<String, String> headersMap)
	{
		CollectionProperty headsArrayList =getHeaderManager();
		if (headsArrayList!=null) {
			int count = headsArrayList.size();
			for (int rowN = 0; rowN < count; rowN++) {
				Header header = (Header) headsArrayList.get(rowN).getObjectValue();
				String name=header.getName();
				String value =header.getValue();
				headersMap.put(name, value);
			}
		}
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
		String url="";
		if (getMethod().startsWith("/")) {
			url=String.format("https://%s%s", getServer(),getMethod());
		}else {
			url=String.format("https://%s/%s", getServer(),getMethod());
		}
		res.setHTTPMethod("POST");
		Map<String, String> headers=getUserDefinedHeaders();
		//Add HTTP信息头管理器
		setConnectionHeaders(headers);
		
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
		String queryString =ApiTool.queryString(paramsMap);
		res.setQueryString(queryString);


		int file_upLoad_size = 0;
		//ApiTool.post(res,url, headers, paramsMap);
		JMeterProperty threadValues = getData();
		if (!(threadValues instanceof NullProperty)) {
			CollectionProperty columns = (CollectionProperty)threadValues;
			int count = columns.size();
			//upload file
			if (count>=1) {
				HttpsPostFile postUtil = new HttpsPostFile(res,url);
				try {
					postUtil.setHeaders(headers);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				postUtil.addTextParameter(paramsMap);
				for (int rowN = 0; rowN < count; rowN++) {
					@SuppressWarnings("unchecked")
					ArrayList<JMeterProperty> rowObject = (ArrayList<JMeterProperty>) columns.get(rowN).getObjectValue();
					boolean isNotBlank=false;
					for (int i = 0; i < columnClasses.length; i++) {
						String data = rowObject.get(i).getStringValue();
						if (!StringUtils.isNotBlank(data)){
							isNotBlank=true;
							break;
						}
					}
					if (!isNotBlank) {
						String _file_path = rowObject.get(0).getStringValue();
						String argumentName = rowObject.get(1).getStringValue();
						postUtil.addFileParameter(argumentName,new File(_file_path) );
						file_upLoad_size=file_upLoad_size+1;
					}
				}
				if (file_upLoad_size>=1) {
					postUtil.send();
				}
			}
		}
		if (file_upLoad_size==0) {
			//send data with text
			HttpsPostText postText = new HttpsPostText(res,url);
			postText.setHeaders(headers);
			postText.setTextParams(paramsMap);
			postText.send();
		}
		res.setSampleLabel(getName());
		//res.setResponseData("setResponseData", null);
		res.setDataType(SampleResult.TEXT);
		//String varLogsString = storeResponseVaribles(getThreadContext(), new String(res.getResponseData()),getStoredVariables());
		if(res.isSuccessful()){
			String varLogsString = storeResponseVaribles(getThreadContext(), new String(res.getResponseData()),getStoredVariables(),"");
			log.info(varLogsString);
		}
		//res.setResponseData(new String(res.getResponseData())+varLogsString,null);
		//res.setResponseData(new String(res.getResponseData()),null);
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
	public void setFilesUpLoad(Arguments vars) {
		setProperty(new TestElementProperty(USER_DEFINED_FILES_UPLOAD, vars));
	}
	public JMeterProperty getFilesUpLoadAsProperty() {
		return getProperty(USER_DEFINED_FILES_UPLOAD);
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
	public void setData(CollectionProperty rows)
	{
		setProperty(rows);
	}
}