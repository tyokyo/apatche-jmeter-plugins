package sioeye.spider.helpers;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SpiderHelpers {
	/**
	 * get url html页面内容
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public  String crawl(String url) throws Exception{
		try(CloseableHttpClient  httpClient=HttpClientBuilder.create().build();
			CloseableHttpResponse httpResponse=httpClient.execute(new HttpGet(url))){
			String result=EntityUtils.toString(httpResponse.getEntity());
			return result;
		}catch (Exception e) {
			System.out.println("抓取html页面失败");
			throw new RuntimeException(e);
		}
	}
	
}
