package org.apache.jmeter.sampler.util;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
public class JsonParse {
	private static Logger logger =LoggerFactory.getLogger(JsonParse.class);
	private List<Object>result;
	public List<Object> getResult() {
		return result;
	}
	public void setResult(List<Object> result) {
		this.result = result;
	}
	private String jsonString;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	private String queryString;

    public List<Object> parse(){
    	logger.info("QueryString="+getQueryString());
        List<Object> resultList = JsonPath.read(getJsonString(), getQueryString());
        setResult(resultList);
        return resultList;
	}
    public void print(){
    	 for(Iterator<Object> it = getResult().iterator();it.hasNext();) {
             System.out.println(it.next());
         }
    }
}