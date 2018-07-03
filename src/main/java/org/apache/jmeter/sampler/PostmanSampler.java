package org.apache.jmeter.sampler;

import java.io.File;
import java.io.IOException;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.sampler.util.HttpsPostFile;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.samplers.Entry;

public class PostmanSampler extends AbstractSampler {
	private static final long serialVersionUID = 1L;
	public final static String REQUEST_PATH = "postmansampler.request.path"; 
	public final static String REQUEST_METHOD = "postmansampler.request.method";
	public final static String FILE_UPLOAD = "postmansampler.upload.file"; 
	private static Logger log = LoggerFactory.getLogger(PostmanSampler.class);

	public String  getRequsetPath() {
		return getPropertyAsString(REQUEST_PATH);
	}
	public void setRequsetPath(String path) {
		setProperty(REQUEST_PATH, path);
	}
	public String  getMethod() {
		return getPropertyAsString(REQUEST_METHOD);
	}
	public void setMethod(String method) {
		setProperty(REQUEST_METHOD, method);
	}
	public String  getUploadFile() {
		return getPropertyAsString(REQUEST_METHOD);
	}
	public void setUploadFile(String path) {
		setProperty(REQUEST_METHOD, path);
	}
	private void request(String uploadFilepath,String url,HTTPSampleResult res){
		try {
			try {
				HttpsPostFile postUtil = new HttpsPostFile(res,url);
				postUtil.addFileParameter("file",new File(uploadFilepath) );
				postUtil.send("PUT");
				//System.out.println(res.getResponseCode());
				//System.out.println(res.getResponseDataAsString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (res.isSuccessful()) {
				res.setQueryString("PUT :  "+url);
				res.setResponseOK();
			}else {
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public HTTPSampleResult sample(Entry arg0) {
		HTTPSampleResult res = new HTTPSampleResult();
		res.sampleStart();
		res.setHTTPMethod("PUT");
		res.setDataType(SampleResult.TEXT);
		res.setSampleLabel(getName());
		request(getUploadFile(), getRequsetPath(), res);
		res.sampleEnd();
		return res;
	}
}