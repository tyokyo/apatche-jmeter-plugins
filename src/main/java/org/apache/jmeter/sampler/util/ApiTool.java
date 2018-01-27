package org.apache.jmeter.sampler.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.apache.jmeter.functions.String2MD5;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiTool{
	private static Logger logger =LoggerFactory.getLogger(ApiTool.class);
	private static final int ConnectTimeout=20*1000;
	private static final int ReadTimeout=60*1000;
	public static String dealResponseResult(InputStream inputStream,String encode) {
		if(inputStream!=null){
			String resultData = null; 
			GZIPInputStream gZIPInputStream = null;
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int len = 0;
			try {
				if(encode!=null&&encode.equals("gzip")){
					gZIPInputStream = new GZIPInputStream(inputStream);//gzip
					while((len = gZIPInputStream.read(data)) != -1) {
						byteArrayOutputStream.write(data, 0, len);
					}
				}else{
					while((len = inputStream.read(data)) != -1) {
						byteArrayOutputStream.write(data, 0, len);
					}
				}
			} catch (IOException e) {
				logger.error("Exception",e);
			}finally {			
				try {
					if(byteArrayOutputStream!=null){
						byteArrayOutputStream.close();
					}
					if(gZIPInputStream!=null){
						gZIPInputStream.close();
					}
				} catch (IOException e) {
					logger.error("Exception",e);
				}
			}
			try {
				resultData = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
				byteArrayOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Exception",e);
			}
			return resultData;
		}else{
			return null;	
		}
	}
	public static String getSamplerData(String api,Map<String, String> header,Map<String, String> paramsMap){
		StringBuffer buffer = new StringBuffer();
		buffer.append("POST:"+api+"\n");
		buffer.append("\n\n");
		buffer.append("POST data:"+"\n");
		buffer.append("{\n");
		for (String pamKey : paramsMap.keySet()) {
			String pamValue=paramsMap.get(pamKey);
			buffer.append(String.format("%s : %s\n", pamKey,pamValue));
		}
		buffer.append("}\n");
		return buffer.toString();
	}
	public static String getHeaderStrings(Map<String, String> header){
		StringBuffer headerBuffer=new StringBuffer();
		for (String key : header.keySet()) {
			String value = header.get(key);
			String lineHeader=String.format("%s:%s", key,value);
			headerBuffer.append(lineHeader+"\n");
		}
		return headerBuffer.toString();
	}
	public static String queryString(Map<String, String> paramsMap){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		for (String key : paramsMap.keySet()) {
			String value = paramsMap.get(key);
			String line = String.format("%s:%s", key,value);
			buffer.append(line+"\n");
		}
		buffer.append("}");
		return buffer.toString();
	}
	public static void errorResult(Throwable e, SampleResult res)
	  {
	    res.setSampleLabel(res.getSampleLabel());
	    res.setDataType("text");
	    java.io.ByteArrayOutputStream text = new java.io.ByteArrayOutputStream(200);
	    e.printStackTrace(new PrintStream(text));
	    res.setResponseData(text.toByteArray());
	    res.setResponseCode(new StringBuilder().append("Non HTTP response code: ").append(e.getClass().getName()).toString());
	    res.setResponseMessage(new StringBuilder().append("Non HTTP response message: ").append(e.getMessage()).toString());
	    res.setSuccessful(false);
	  }
	public static void doSessonToken(String result){
		try {
			JsonParse js = new JsonParse();
			js.setJsonString(result);
			js.setQueryString(".sessiontoken");
			List<Object> skens = js.parse();
			if (skens.size()==1) {
				String sessiontoken =skens.get(0).toString();
				JMeterUtils.getJMeterProperties().put("sessiontoken", sessiontoken);
			}
			//js.print();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void post(HTTPSampleResult res,String api,Map<String, String> header,Map<String, String> paramsMap) {
		String result=null;
		InputStream inptStream=null;
		InputStream errorStream=null;
		OutputStream outputStream=null;
		URL url = null;
		long Stime=0,Etime=0;
		int httpConnectionCode = -1;
		HttpsURLConnection httpsURLConnection=null;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String params=JSONObject.fromObject(paramsMap).toString();
		try {
			url = new URL(api);
			byte[] data = params.getBytes();
			httpsURLConnection = (HttpsURLConnection)url.openConnection();
			httpsURLConnection.setConnectTimeout(ConnectTimeout);      
			httpsURLConnection.setReadTimeout(ReadTimeout);
			httpsURLConnection.setDoInput(true);                  
			httpsURLConnection.setDoOutput(true);                 
			httpsURLConnection.setRequestMethod("POST");   
			httpsURLConnection.setUseCaches(false);         
			//SSLContext
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext;
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new java.security.SecureRandom());
			//SSLSocketFactory
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			httpsURLConnection.setSSLSocketFactory(ssf);
			Set<String> e = header.keySet();
			for (String key : e) {
				Object value = header.get(key);
				httpsURLConnection.setRequestProperty(key,value.toString());
				//X_Sioeye_App_Id  x_sioeye_app_sign_key X_Sioeye_App_Production  X_sioeye_sessiontoken Content-Type
			}
			httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));

			Stime=new Date().getTime();
			outputStream = httpsURLConnection.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			httpConnectionCode = httpsURLConnection.getResponseCode(); 
			//设置是否连接成功
			//bean.setHttpConnectionCode(HttpsURLConnection.HTTP_OK);
			Etime=new Date().getTime();
			String encode=httpsURLConnection.getContentEncoding();
			if(httpConnectionCode == HttpsURLConnection.HTTP_OK) {
				inptStream = httpsURLConnection.getInputStream();
				result=dealResponseResult(inptStream,encode);
				res.setResponseData(JSONObject.fromObject(result).toString().replaceAll(",\"", ",\n\"").replace("{\"", "{\n\"").replace("}}", "}\n}"), null);
			}else{
				errorStream= httpsURLConnection.getErrorStream();
				result=dealResponseResult(errorStream,encode);
				//Jsonpath.parse(result, ".value.*");
				res.setResponseData(JSONObject.fromObject(result).toString().replaceAll(",\"", ",\n\"").replace("{\"", "{\n\"").replace("}}", "}\n}"), null);;
			}
			doSessonToken(result);
			httpsURLConnection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errorResult(e, res);
			res.sampleEnd();
			logger.warn(e.getMessage());
			res.setResponseMessage(e.getMessage());
		}finally {
			try {
				if(inptStream!=null) {
					inptStream.close();
				}
				if(outputStream!=null) {
					outputStream.close();
				}
				if(errorStream!=null){
					errorStream.close();
				}
				if(sw!=null){
					sw.close();
				}
				if(pw!=null){
					pw.close();
				}
			} catch (IOException e) {
				logger.error("Exception",e);
			}
		}
		long responseTime = Etime-Stime;
	}
	public static void main(String args[]){
		String url ="https://api.siocloud.sioeye.cn/functions/login" ;
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("username","tyokyo@126.com");
        params.put("password",String2MD5.MD5("123456789"));
	    params.put("type","app");
	    
	    HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json;charset=utf-8");
		header.put("X_Sioeye_App_Id", "usYhGBBKDMiypaKFV8fc3kE4");
		header.put("X_Sioeye_App_Sign_Key", "5f3773d461775804ca2c942f8589f1d6,1476178217671");
		header.put("X_Sioeye_App_Production", "1");
		//post(url, header, params);
	}
}
