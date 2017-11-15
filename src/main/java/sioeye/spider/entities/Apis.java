package sioeye.spider.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Apis implements Serializable {
	private String apiName;
	private String apiServerUrl;  
	private String apiDocUrl;
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getApiServerUrl() {
		return apiServerUrl;
	}
	public void setApiServerUrl(String apiServerUrl) {
		this.apiServerUrl = apiServerUrl;
	}
	public String getApiDocUrl() {
		return apiDocUrl;
	}
	public void setApiDocUrl(String apiDocUrl) {
		this.apiDocUrl = apiDocUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiDocUrl == null) ? 0 : apiDocUrl.hashCode());
		result = prime * result + ((apiName == null) ? 0 : apiName.hashCode());
		result = prime * result + ((apiServerUrl == null) ? 0 : apiServerUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Apis other = (Apis) obj;
		if (apiDocUrl == null) {
			if (other.apiDocUrl != null)
				return false;
		} else if (!apiDocUrl.equals(other.apiDocUrl))
			return false;
		if (apiName == null) {
			if (other.apiName != null)
				return false;
		} else if (!apiName.equals(other.apiName))
			return false;
		if (apiServerUrl == null) {
			if (other.apiServerUrl != null)
				return false;
		} else if (!apiServerUrl.equals(other.apiServerUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Apis [apiName=" + apiName + ", apiServerUrl=" + apiServerUrl + ", apiDocUrl=" + apiDocUrl + "]";
	}

}
