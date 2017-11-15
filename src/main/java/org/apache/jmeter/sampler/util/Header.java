package org.apache.jmeter.sampler.util;

import java.util.HashMap;

public class Header {
	public static HashMap<String, Object> getHeader(){
		HashMap<String, Object> header = new HashMap<String, Object>();
		header.put("Content-Type", "application/json;charset=utf-8");
		header.put("X_Sioeye_App_Id", "usYhGBBKDMiypaKFV8fc3kE4");
		header.put("X_Sioeye_App_Sign_Key", "5f3773d461775804ca2c942f8589f1d6,1476178217671");
		header.put("X_Sioeye_App_Production", "1");
		return header;
	}
}
