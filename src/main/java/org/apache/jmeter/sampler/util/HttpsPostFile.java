package org.apache.jmeter.sampler.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.sampler.util.MyX509TrustManager;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 以Http协议传输文件
 * 
 * @author mingxue.zhang@163.com
 *
 */
public class HttpsPostFile{
	private  Logger logger =LoggerFactory.getLogger(HttpsPostText.class);
	private HTTPSampleResult res;
	private  InputStream postInuptStream=null;
	private  InputStream  postErrorStream=null;
	private OutputStream connOutStream;
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private int connCode = -1;
	private URL url;
	private HttpsURLConnection conn;
	private String boundary = null;
	private Map<String, String> textParams = new HashMap<String, String>();
	private Map<String, File> fileparams = new HashMap<String, File>();

	public HttpsPostFile(HTTPSampleResult res,String url) {
		try {
			this.url = new URL(url);
			this.res=res;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 重新设置要请求的服务器地址，即上传文件的地址。
	public void setUrl(String url) throws Exception {
		this.url = new URL(url);
	}

	// 增加一个普通字符串数据到form表单数据中
	public void addTextParameter(String name, String value) {
		textParams.put(name, value);
	}
	public void addTextParameter(Map<String, String> textMaps) {
		Set<String> keys = textMaps.keySet();
		for (String name : keys) {
			String value=textMaps.get(name);
			textParams.put(name, value);
		}
	}

	// 增加一个文件到form表单数据中
	public void addFileParameter(String name, File value) {
		fileparams.put(name, value);
	}

	// 清空所有已添加的form表单数据
	public void clearAllParameters() {
		textParams.clear();
		fileparams.clear();
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
	private  String coverInputStreamResult(InputStream inputStream) {
		String encode=conn.getContentEncoding();
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
			//logger.info(resultData);
			return resultData;
		}else{
			return null;	
		}
	}
	private void errorResult(Throwable e, SampleResult res)
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
	/**
	 * 发送数据到服务器
	 */
	public void send(){
		try {
			initConnection();
			conn.connect();
			connOutStream = new DataOutputStream(conn.getOutputStream());

			writeFileParams(connOutStream);
			writeStringParams(connOutStream);
			writesEnd(connOutStream);

			//read response data
			connCode = conn.getResponseCode(); 
			//connect success
			if(connCode == HttpsURLConnection.HTTP_OK) {
				postInuptStream = conn.getInputStream();
				String response=coverInputStreamResult(postInuptStream);
				res.setResponseData(JsonFormatUtil.formatJson(response),null);
				res.setSuccessful(true);
				doSessonToken(response);
			}else{
				postErrorStream=conn.getErrorStream();
				String response=coverInputStreamResult(postErrorStream);
				res.setResponseData(response,null);
				res.setSuccessful(false);
			}
		} catch (Exception e) {
			errorResult(e, res);
			res.sampleEnd();
			logger.warn(e.getMessage());
		}finally{
			try {
				if (connOutStream!=null) {
					connOutStream.close();
				}
				if (conn!=null) {
					conn.disconnect();
				}
				if (postInuptStream!=null) {
					postInuptStream.close();
				}
				if (postErrorStream!=null) {
					postErrorStream.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	// 文件上传的connection的一些必须设置
	private void initConnection() throws Exception {
		StringBuffer buf = new StringBuffer("----");
		Random rand = new Random();
		for (int i = 0; i < 15; i++) {
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		this.boundary = buf.toString();

		conn = (HttpsURLConnection) this.url.openConnection();
		//SSLContext
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext;
		sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, tm, new java.security.SecureRandom());
		//SSLSocketFactory
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		conn.setSSLSocketFactory(ssf);

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(30 * 1000); // 连接超时为10秒
		conn.setRequestMethod("POST");
		conn.setReadTimeout(30*1000);

		conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);

	}

	// 普通字符串数据
	private void writeStringParams(OutputStream out) throws Exception {
		Set<String> keySet = textParams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParams.get(name);

			out.write(("--" + boundary + "\r\n").getBytes());
			out.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n")
					.getBytes());
			out.write(("\r\n").getBytes());
			out.write((encode(value) + "\r\n").getBytes());
		}
	}

	// 文件数据
	private void writeFileParams(OutputStream out) throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = fileparams.get(name);

			out.write(("--" + boundary + "\r\n").getBytes());
			out.write(("Content-Disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + encode(value.getName()) + "\"\r\n")
					.getBytes());
			out.write(("Content-Type: " + getContentType(value) + "\r\n")
					.getBytes());
			out.write(("Content-Transfer-Encoding: " + "binary" + "\r\n")
					.getBytes());

			out.write(("\r\n").getBytes());

			FileInputStream inStream = new FileInputStream(value);
			int bytes = 0;
			byte[] bufferByte = new byte[1024];
			while ((bytes = inStream.read(bufferByte)) != -1) {
				out.write(bufferByte, 0, bytes);
			}
			inStream.close();

			out.write(("\r\n").getBytes());
		}
	}

	// 添加结尾数据
	private void writesEnd(OutputStream out) throws Exception {
		out.write(("--" + boundary + "--" + "\r\n").getBytes());
		out.write(("\r\n").getBytes());
	}

	// 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	private String getContentType(File f) throws Exception {
		String fileName = f.getName();
		if (fileName.endsWith(".jpg")) {
			return "image/jpeg";
		} else if (fileName.endsWith(".png")) {
			return "image/png";
		}
		return "application/octet-stream";
	}

	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}

}