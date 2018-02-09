package com;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpsUrl {
	/**
	 * HttpsURLConnection上传文件流
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void sendPostWithFile(){
		String url= "https://face.sioeye.cn/face/mface/search_face";
		String file_png = "D:/soft/Jmeter/photo/11hongjie.png";
		try {
			HttpPostUtil postUtil = new HttpPostUtil(url);
			postUtil.addTextParameter("current_rotate", "0");
			postUtil.addTextParameter("openid", "ovZTu0OqqEhLPeny_art2m8NntvI");
			postUtil.addTextParameter("conversationid", "b411b46afdc4434aa858cb153083d63e");
			postUtil.addTextParameter("aid", "59015edeec664952bc77980c8d265441");
			postUtil.addFileParameter("file",new File(file_png) );

			byte[]  resultByte = postUtil.send();
			String resultString=new String(resultByte);
			System.out.println(resultString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void sendPostNoFile(){
		String url= "https://api.siocloud.sioeye.cn/functions/login";
		try {
			HttpPostUtil postUtil = new HttpPostUtil(url);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("X_Sioeye_App_Id", "usYhGBBKDMiypaKFV8fc3kE4");
			headers.put("X_Sioeye_App_Sign_Key", "5f3773d461775804ca2c942f8589f1d6,1476178217671");
			headers.put("X_Sioeye_App_Production", "1");
			
			postUtil.addTextParameter("type", "web");
			postUtil.addTextParameter("username", "tyokyo@126.com");
			postUtil.addTextParameter("password", "25f9e794323b453885f5181f1b624d0b");
			
			byte[]  resultByte = postUtil.send();
			String resultString=new String(resultByte);
			System.out.println(resultString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)  {
		sendPostWithFile();
		//sendPostNoFile();
	}
}
