package org.apache.jmeter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tool {
	public static void writeToFile(String path,String content){
		try {     
			FileWriter writer = new FileWriter(path, true);     
			writer.write(content);     
			writer.close();     
		} catch (IOException e) {     
			e.printStackTrace();     
		}     
	}
	public static String getDate(){
		Date d = new Date();    
		String s = null;    
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		s = sdf.format(d);    
		return s;
	}
	public static Map<Object, Object> mapToObj(Map<String, String> map){
		Map<Object, Object> objMap = new HashMap<Object, Object>();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			String value = map.get(key);
			String start = "[";
			String end = "]";
			if (value.startsWith(start)&&value.endsWith(end)) {
				value=value.replace("[", "");
				value=value.replace("]", "");
				objMap.put(key, value.split(","));
			}else {
				objMap.put(key, value);
			}
		}
		return objMap;
	}
}
