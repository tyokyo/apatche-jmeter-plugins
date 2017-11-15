package org.apache.jmeter.sampler.util;
public class IResult {
	private String responseData="";
	private long ConnectTimeout=20*1000;
	private long ReadTimeout=60*1000;
	private long httpResponseTime=0;
	private int httpConnectionCode=200;
}
