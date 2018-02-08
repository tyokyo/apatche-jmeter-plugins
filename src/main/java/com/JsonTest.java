package com;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class JsonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String json = "...";
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		String author0 = JsonPath.read(document, "$.store.book[0].author");
		String author1 = JsonPath.read(document, "$.store.book[1].author");
		
	}

}
