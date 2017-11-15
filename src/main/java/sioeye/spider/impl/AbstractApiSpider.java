package sioeye.spider.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sioeye.spider.entities.Apis;
import sioeye.spider.helpers.PropertyHelpers;
import sioeye.spider.helpers.SpiderHelpers;
import sioeye.spider.interfaces.IApiSpider;

public class AbstractApiSpider implements IApiSpider {

	@Override
	public List<Apis> getApi(String apidocurl) {
		try {
			String result = new SpiderHelpers().crawl(apidocurl);
			Document doc=Jsoup.parse(result);
			//apiName,apiUrl
			Elements apiNameUrl=doc.select(".index-list.methods li a");
			List<Apis> apis= new ArrayList<>();
			for(Element a:apiNameUrl){
				Apis api=new Apis();
				String apiName= a.text();
				String apiServerUrl=new PropertyHelpers().getServerUrl()+apiName;
				api.setApiName(apiName);
				api.setApiServerUrl(apiServerUrl);
				api.setApiDocUrl(apidocurl);
				apis.add(api);
			}
			return apis;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
