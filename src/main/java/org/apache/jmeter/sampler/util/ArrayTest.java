package org.apache.jmeter.sampler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

public class ArrayTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Object, Object> map = new HashMap<Object, Object>();
		ArrayList<String> session = new ArrayList<String>();
		session.add("session1");
		session.add("session2");
		
		map.put("[sessions]", session);
		map.put("name", "tyokyo");
		String json = JSONObject.fromObject(map).toString();;
		System.out.println(json);
		
		Object[] ids = new Object[2];
		ids[0]="0f1a9a8d655c45b5a9f61cb991615b79";
		ids[1]="704f7ee5130a476aa2b3b0fd450f818f";
        
	}

}
