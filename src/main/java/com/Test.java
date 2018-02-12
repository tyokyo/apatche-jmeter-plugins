package com;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long timeStampSec = System.currentTimeMillis()/1000+10*60;
		String timestamp = String.format("%010d", timeStampSec);
		System.out.println(timestamp);

		timeStampSec = timeStampSec+60*60;
		timestamp = String.format("%010d", timeStampSec);
		System.out.println(timestamp);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(System.currentTimeMillis());
		Date date = new Date(lt);
		String    res = simpleDateFormat.format(date);
		System.out.println(res);
	}
}
