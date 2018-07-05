package org.apache.jmeter.sampler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
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
	private String getContentType(String  pathname) throws Exception {
		File f = new File(pathname);
		String fileName = f.getName();
		if (fileName.endsWith(".jpg")) {
			return "image/jpeg";
		} else if (fileName.endsWith(".png")) {
			return "image/png";
		}else if (fileName.endsWith(".mp4")) {
			return "video/mp4";
		}else {
			return "";
		}
	}
	public static String getErrorInfoFromException(Exception e) {  
		try {  
			StringWriter sw = new StringWriter();  
			PrintWriter pw = new PrintWriter(sw);  
			e.printStackTrace(pw);  
			return "\r\n" + sw.toString() + "\r\n";  
		} catch (Exception e2) {  
			return "bad getErrorInfoFromException";  
		}  
	}
	private void request(String uploadFilepath,String url,HTTPSampleResult res) {
		OkHttpClient client = new OkHttpClient();
		//MediaType mediaType = MediaType.parse("video/mp4; charset=utf-8");
		MediaType mediaType=null;
		try {
			mediaType = MediaType.parse(getContentType(uploadFilepath)+"; charset=utf-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File file = new File(uploadFilepath);
		RequestBody body = RequestBody.create(mediaType, file);
		Request request = new Request.Builder()
		.url(url)
		.put(body)
		.addHeader("Cache-Control", "no-cache")
		.addHeader("Postman-Token", "16cb6211-565f-43dc-b729-5753d2351ec9")
		.build();
		res.setRequestHeaders(request.headers().toString());
		
		try {
			Response response = client.newCall(request).execute();
			if (response.code()==200) {
				String msg = String.format("%d\n%s", response.code(),response.toString());
				res.setResponseData(msg,null);
				res.setResponseCodeOK();
				res.setSuccessful(true);
			}else {
				res.setResponseData(response.toString(),null);
				res.setSuccessful(false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			String errorMsg = getErrorInfoFromException(e);
			log.info(errorMsg);
			res.setResponseData(errorMsg.getBytes());
			res.setResponseCode(3000+"");
			res.setResponseMessage(errorMsg);
			res.setSuccessful(false);
		}
		
}
@Override
public HTTPSampleResult sample(Entry arg0) {
	HTTPSampleResult res = new HTTPSampleResult();
	res.sampleStart();
	res.setDataEncoding("UTF-8");
	res.setHTTPMethod("PUT:"+getRequsetPath()+"\n"+getUploadFile());
	res.setContentType("application/octet-stream");
	res.setDataType(SampleResult.BINARY);
	res.setSampleLabel(getName());
	//request(getUploadFile(), getRequsetPath(), res);
	try {
		request(getUploadFile(), getRequsetPath(), res);
	} catch (Exception e) {
		// TODO Auto-generated catch block
	}
	res.sampleEnd();
	return res;
}
}