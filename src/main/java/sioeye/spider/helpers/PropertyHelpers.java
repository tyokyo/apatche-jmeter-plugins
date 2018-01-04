package sioeye.spider.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import kg.apc.jmeter.JMeterPluginsUtils;

public class PropertyHelpers {
	
	/**
	 * 获取properties文件对象
	 * @return
	 * @throws Exception
	 */
	private Properties loadConfigPro(){
		Properties properties = new Properties();
		FileInputStream fis=null;
		try {
			String jmeter_home=System.getenv("JMETER_HOME");
			
			fis = new FileInputStream(jmeter_home+File.separator+"lib"+File.separator+"ext"+File.separator+"properties"+File.separator+"config.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	private static Properties loadHeaderPro(){
		Properties properties = new Properties();
		FileInputStream fis=null;
		try {
			String jmeter_home=System.getenv("JMETER_HOME");
			
			fis = new FileInputStream(jmeter_home+File.separator+"lib"+File.separator+"ext"+File.separator+"properties"+File.separator+"header.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			properties.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	public  static HashMap<String, String> getHeaderMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		Properties p = loadHeaderPro();
        Enumeration<?> enum1 = p.propertyNames();
        while(enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = p.getProperty(strKey);
            map.put(strKey, strValue);
        }
        return map;
	}
	/**
	 * 获取服务器api域名
	 * @return
	 */
	public String getServerUrl(){
		String serverurl = loadConfigPro().get("serverurl").toString();
		return serverurl;
	}
	
	/**
	 * 获取服务器api doc url
	 * @return
	 */
	public String getApiDocUrl(){
		String apidocurl=loadConfigPro().get("apidocurl").toString();
		return apidocurl;
	}
	public static String getKey(String key){
		String value = "";
		Properties properties = new Properties();
		InputStream fis=null;
		try {
			fis = JMeterPluginsUtils.class.getResourceAsStream("properties/config.properties");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			properties.load(fis);
			value = properties.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
