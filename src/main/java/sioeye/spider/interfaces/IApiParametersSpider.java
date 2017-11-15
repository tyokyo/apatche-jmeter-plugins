package sioeye.spider.interfaces;

import java.util.List;

import sioeye.spider.entities.ApiParameters;

public interface IApiParametersSpider {
	/**
	 * 获取接口的传参详细信息
	 * @param apidocurl
	 * @param apiname
	 * @return {apiParameterName, apiParameterType, apiParameterFlag, apiParameterDesc}
	 */
	public List<ApiParameters> getApiParameters(String apidocurl,String apiname);
}
