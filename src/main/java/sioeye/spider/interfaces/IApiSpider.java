package sioeye.spider.interfaces;

import java.util.List;

import sioeye.spider.entities.Apis;

public interface IApiSpider {
	
	/**
	 * 获取任意一个接口场景下的所有api内容()
	 * @param apidocurl
	 * @return{apiName,apiServerUrl}
	 */
	public List<Apis> getApi(String apidocurl);
}
