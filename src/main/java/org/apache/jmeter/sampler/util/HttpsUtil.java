package org.apache.jmeter.sampler.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.crypto.Data;

public class HttpsUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);
	public static String Server;
	public static String X_Sioeye_App_Id;
	public static String X_Sioeye_App_Key;
	public static String X_Sioeye_App_Production;
	public static int serverTimeout;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	private  final static String CR_LF = "\r\n";  
	private  final static String TWO_DASHES = "--";  
	public static boolean HttpsDebug=true;
	public static List<Object> sendPostData(String api,String sessiontoken,String params) {
		List<Object> list=new ArrayList<Object>();
		String result=null;
		InputStream inptStream=null;
		InputStream errorStream=null;
		OutputStream outputStream=null;
		URL url = null;
		long Stime=0,Etime=0;
		int resposeOK=-1;
		int resposeCode = -1;
		HttpsURLConnection httpsURLConnection=null;
		JSONObject jsonObject=null;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			url = new URL(Server+api);
			byte[] data = params.getBytes();
			httpsURLConnection = (HttpsURLConnection)url.openConnection();
			httpsURLConnection.setConnectTimeout(serverTimeout);
			httpsURLConnection.setReadTimeout(serverTimeout);
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
			httpsURLConnection.setRequestProperty("X_Sioeye_App_Id", X_Sioeye_App_Id);
			httpsURLConnection.setRequestProperty("x_sioeye_app_sign_key", X_Sioeye_App_Key);
			httpsURLConnection.setRequestProperty("X_Sioeye_App_Production", X_Sioeye_App_Production);
			if(sessiontoken!=null){
				httpsURLConnection.setRequestProperty("X_sioeye_sessiontoken", sessiontoken);
			}
			
			httpsURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			
			httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
			Stime=new Date().getTime();
			
			outputStream = httpsURLConnection.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			resposeCode = httpsURLConnection.getResponseCode();       
			Etime=new Date().getTime();
			String encode=httpsURLConnection.getContentEncoding();
			if(resposeCode == HttpsURLConnection.HTTP_OK) {
				resposeOK=1;
				inptStream = httpsURLConnection.getInputStream();
				result=dealResponseResult(inptStream,encode);                 
			}else{
				resposeOK=0;
				errorStream= httpsURLConnection.getErrorStream();
				result=dealResponseResult(errorStream,encode);
				jsonObject=new JSONObject("{\"success\":\"error2\",\"value\":\"HttpFail\"}");
			}
			httpsURLConnection.disconnect();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
			e.printStackTrace(pw);
			result=sw.toString();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
			e.printStackTrace(pw);
			result=sw.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
			e.printStackTrace(pw);
			result=sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
			e.printStackTrace(pw);
			result=sw.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
			e.printStackTrace(pw);
			result=sw.toString();
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
		if(HttpsDebug){
			if(resposeOK==-1){//exception
				logger.warn("API="+api+",type=sendPostData"+",get info from server failed with exception!");
			}else if(resposeOK==1){//ok
				logger.info("API="+api+",Resposetime="+(Etime-Stime)+"ms,Code="+resposeCode+",type=sendPostData");
				logger.info("API="+api+",result="+result);
			}else if(resposeOK==0){//http fail
				logger.warn("API="+api+",Resposetime="+(Etime-Stime)+"ms,ErrorCode="+resposeCode+",type=sendPostData");
				logger.warn("API="+api+",result="+result);
			}	
		}
		list.add(jsonObject);
		list.add(result);
		list.add((Etime-Stime)+"");
		list.add(resposeCode);
		return list;
	}
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
	public static class MyX509TrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	public static String setBOUNDARY() {
		char[] MULTIPART_CHARS ="1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();  
		StringBuilder buffer = new StringBuilder();  
		Random rand = new Random();  
		int count = rand.nextInt(11) + 30; // a random size from 30 to 40   
		for (int i = 0; i < count; i++) {  
			buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);  
		}  
		String BOUNDARY =  buffer.toString();  
		logger.info("BOUNDARY= "+BOUNDARY);
		return BOUNDARY;
	}
}
