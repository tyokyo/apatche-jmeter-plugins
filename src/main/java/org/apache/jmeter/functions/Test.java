package org.apache.jmeter.functions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String _p_value="";
		Properties prop = new Properties();     
		//读取属性文件a.properties
		try {
			InputStream  in = new FileInputStream("D:\\soft\\Jmeter\\apache-jmeter-3.3\\data\\config.properties");
			prop.load(in);     ///加载属性列表
			Iterator<String> it=prop.stringPropertyNames().iterator();
			while(it.hasNext()){
				String _p_key=it.next();
				_p_value=prop.getProperty(_p_key);
				System.out.println(_p_key+":"+prop.getProperty(_p_key));
			}
			in.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
