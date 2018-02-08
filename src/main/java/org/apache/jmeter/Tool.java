package org.apache.jmeter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
