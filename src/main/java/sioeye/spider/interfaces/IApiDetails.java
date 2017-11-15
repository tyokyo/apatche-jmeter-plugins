package sioeye.spider.interfaces;

import java.util.List;

import sioeye.spider.entities.ApiDetails;

public interface IApiDetails {
	
	/**
	 * 获取接口的详细信息，包括接口的描述、参数（参数描述，类型，可选等）、返回信息
	 * @param apidocurl
	 * @param apiname
	 * @return
	 */
	public List<ApiDetails> getApiDetails(String apidocurl, String apiname);
}
