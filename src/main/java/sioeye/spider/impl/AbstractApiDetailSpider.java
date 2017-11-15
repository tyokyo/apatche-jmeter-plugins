package sioeye.spider.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import sioeye.spider.entities.ApiDetails;
import sioeye.spider.helpers.SpiderHelpers;
import sioeye.spider.interfaces.IApiDetails;

public class AbstractApiDetailSpider implements IApiDetails {
	
	
	@Override
	public List<ApiDetails> getApiDetails(String apidocurl, String apiname) {
		String result=null;
		List<ApiDetails> details = null;
		try {
			details=new ArrayList<>();
			result = new SpiderHelpers().crawl(apidocurl);
			Document doc=Jsoup.parse(result);
			Elements apiNameUrl = doc.select("#method_"+apiname+",.method item");
			Elements apiDesc = apiNameUrl.select(".stdoc-code p");
			Elements apiReturnDesc = apiNameUrl.select(".returns-description p");
			Elements apiReturn = apiNameUrl.select("pre");
			ApiDetails detail = new ApiDetails();
			detail.setApiDesc(apiDesc.text());
			detail.setApiReturn(apiReturn.text());
			detail.setApiReturnDesc(apiReturnDesc.text());
			details.add(detail);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return details;
	}

}
