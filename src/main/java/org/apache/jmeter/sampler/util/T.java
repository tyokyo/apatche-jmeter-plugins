package org.apache.jmeter.sampler.util;

public class T {

	public static void main(String[] args) {
		String key="a=";
		String keyStore="";
		// TODO Auto-generated method stub
		String[] kkey = key.split("=");
		if (kkey.length==2) {
			if (!"".equals(kkey[0])) {
				key=kkey[0];
				keyStore=kkey[1];
			}
		}else {
			key="";
		}
		System.out.println(key);
		System.out.println(keyStore);
	}
}
