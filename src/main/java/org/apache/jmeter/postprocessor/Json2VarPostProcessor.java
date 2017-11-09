package org.apache.jmeter.postprocessor;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json2VarPostProcessor extends AbstractTestElement implements Cloneable, Serializable, PostProcessor, TestElement {
	private static final Logger log = LoggerFactory.getLogger(Json2VarPostProcessor.class);
	private static final long serialVersionUID = 1L;
	private static final String PROPSTATUS= "PROPSTATUS";
	private static final String VARSSTATUS = "VARSSTATUS";
	public static boolean isJsonString(String valueString){
		JSONArray arry = JSONArray.fromObject("["+valueString+"]");
		if (arry.size()>=2) {
			return true;
		}else {
			return false;
		}
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
	public void iterationJson(JMeterContext jMeterContext,String json){
		JSONObject obj=null;
		try {
			obj = JSONObject.fromObject(json);
		} catch (Exception e) {

		}
		if (obj!=null) {
			@SuppressWarnings("unchecked")
			Set<String> keys = obj.keySet();
			if (keys!=null) {
				if (keys.size()>=1) {
					for (String key : keys) {
						String value = obj.getString(key);
						//log.info("Conver Json-"+key+"="+value);
						if (getVars()) {
							JMeterVariables variables = jMeterContext.getVariables();
							variables.put(key, value);
						}else {
							JMeterUtils.getJMeterProperties().put(key, value);
						}
						iterationJson(jMeterContext,value);
					}
				}
			}
		}
	}
	@Override
	public void process() {
		// TODO Auto-generated method stub
		JMeterContext threadContext = getThreadContext();
		String responseString = threadContext.getPreviousResult().getResponseDataAsString();
		log.info(responseString);
		iterationJson( threadContext,responseString);
		JMeterVariables variables = threadContext.getVariables();
		Set<Entry<String, Object>> vEntries = variables.entrySet();
		for (Entry<String, Object> entry : vEntries) {
			//log.info("Add Variables-"+entry.getKey()+"="+entry.getValue().toString());
		}
		//log.info("---------------------------------------------------------------------------------");
		//log.info(JMeterUtils.getJMeterProperties().toString());
	}
}
