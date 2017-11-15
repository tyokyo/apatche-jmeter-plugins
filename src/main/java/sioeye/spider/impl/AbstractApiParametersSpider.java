package sioeye.spider.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sioeye.spider.entities.ApiParameters;
import sioeye.spider.helpers.SpiderHelpers;
import sioeye.spider.interfaces.IApiParametersSpider;

public class AbstractApiParametersSpider implements IApiParametersSpider {

	@Override
	public List<ApiParameters> getApiParameters(String apidocurl, String apiname) {
		List<ApiParameters> parameters=null;
		parameters = new ArrayList<>();
		try {
			String result = new SpiderHelpers().crawl(apidocurl);
			Document doc=Jsoup.parse(result);
			Elements apiNameUrl=doc.select("#method_"+apiname+",.method item");
			Elements paramHtmls = apiNameUrl.select(".params .param");
			for (Element element : paramHtmls){
				Elements typeeElements =element.select(".param .type");
				Elements nameElements =element.select(".param .param-name");
				//Elements paramsNameElements= element.select(".param .param-name.optional");
				Elements paramsDesc = element.select(".param-description p");
				Elements paramsFlag=element.select(".param .flag.optional");
				String flag = paramsFlag==null?"":paramsFlag.text();
				if (nameElements.size()==1) {
					String name = nameElements.text();
					String type = typeeElements.text();
					String desc = paramsDesc.text();

					ApiParameters param = new ApiParameters();
					param.setApiParameterName(name);
					param.setApiParameterDesc(desc);
					param.setApiParameterType(type);
					param.setApiParameterFlag(flag);
					parameters.add(param);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parameters;
	}
	
}
