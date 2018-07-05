package org.apache.jmeter.functions;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {

	public OkHttp() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("https://sioeye-disney-tmp-test.s3.cn-north-1.amazonaws.com.cn/origin/2a3b52219e874440a489c8270ecb334f/5ed4d10545284c71b3dc1e57b16e75a8/c57905798a01471f9ba6388d12c2a28a/videos/048feadb6780447192e92d9d539b4fb0.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAO7QXSMTQKKQGQWRQ%2F20180704%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Date=20180704T094326Z&X-Amz-Expires=900&X-Amz-Signature=5a91b49c70217f80b0ef56717a91ae79cce00ee24dc4822e5ab2aa1e91db816b&X-Amz-SignedHeaders=host%3Bx-amz-acl&x-amz-acl=private")
		  .put(null)
		  .addHeader("Cache-Control", "no-cache")
		  .addHeader("Postman-Token", "d53f7676-a3e5-419f-a526-ff4a62aad867")
		  .build();

		Response response = client.newCall(request).execute();
		System.out.println(response.toString());
	}

}
