package org.apache.jmeter.sampler;

import java.util.Set;

import org.apache.jmeter.Tool;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;

public class VariableSampler extends AbstractSampler {
	private static final long serialVersionUID = 1L;
	public final static String location_path = "VariableSampler.location.location"; 
	public final static String variable_names = "VariableSampler.variable.names";
	private static Logger log = LoggerFactory.getLogger(VariableSampler.class);

	public String  getWriteToPath() {
		return getPropertyAsString(location_path);
	}
	public void setWriteToPath(String _path) {
		setProperty(location_path, _path);
	}
	public String  getVariableNames() {
		return getPropertyAsString(variable_names);
	}
	public void setVariableNames(String varNames) {
		setProperty(variable_names, varNames);
	}
	public static String writeToFileString(JMeterVariables variables,String variableNames,String _path){
		StringBuffer buf = new StringBuffer();
		buf.append(Tool.getDate());
		String[] vars = variableNames.split(",");
		for (int i = 0; i < vars.length; i++) {
			String varValue = "NA";
			String var=vars[i];
			Set<java.util.Map.Entry<String, Object>> vEntries = variables.entrySet();
			for (java.util.Map.Entry<String, Object> entry : vEntries) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				if (key.equals(var)) {
					varValue=value;
					break;
				}
			}
			String appendString = String.format(",%s", varValue);
			buf.append(appendString);
		}
		return buf.toString()+"\n";
	}
	public static String headerString(String variableNames){
		StringBuffer buf = new StringBuffer();
		String[] vars = variableNames.split(",");
		for (int i = 0; i < vars.length; i++) {
			String key = vars[i];
			buf.append(key+",");
		}
		return buf.toString()+"\n";
	}
	@Override
	public HTTPSampleResult sample(Entry arg0) {
		JMeterContext threadContext = getThreadContext();
		JMeterVariables variables = threadContext.getVariables();

		HTTPSampleResult res = new HTTPSampleResult();
		res.sampleStart();
		res.setHTTPMethod("POST");
		res.setDataType(SampleResult.TEXT);

		res.setSampleLabel(getName());
		String path = getWriteToPath();
		String varNames=getVariableNames();
		//String content = headerString(varNames)+writeToFileString(variables, varNames, path);
		String content =writeToFileString(variables, varNames, path);
		Tool.writeToFile(path, content);
		res.setRequestHeaders(path);
		res.setResponseMessage(content);
		res.setResponseData(content,null);
		res.setResponseOK();
		res.sampleEnd();
		return res;
	}
}