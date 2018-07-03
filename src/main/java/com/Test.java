package com;

import java.io.File;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.sampler.util.HttpsPostFile;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url= "https://sioeye-disney-tmp-test.s3.cn-north-1.amazonaws.com.cn/origin/2a3b52219e874440a489c8270ecb334f/11fe28dd7d354656b660106dc3308335/215460550a8f43939f4a95774b64bd59/images/1f02ec7e116743fda76f5c84ba00b7e7.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAO7QXSMTQKKQGQWRQ%2F20180703%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Date=20180703T090204Z&X-Amz-Expires=900&X-Amz-Signature=6a0ce4c583f859a022137ff235ec6cf41b27692ba4ffb9d20bc1169f43dc36e6&X-Amz-SignedHeaders=host%3Bx-amz-acl&x-amz-acl=public-read";
		String file_png = "D:/soft/Jmeter/photo/11hongjie.png";
		try {
			HTTPSampleResult res = new HTTPSampleResult();
			HttpsPostFile postUtil = new HttpsPostFile(res,url);
			postUtil.addFileParameter("file",new File(file_png) );
			postUtil.send("PUT");
			System.out.println(res.getResponseCode());
			System.out.println(res.getResponseDataAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
