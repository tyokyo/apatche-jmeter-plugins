package sioeye.spider.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApiDetails implements Serializable {
	private String apiDesc;
	private String apiParameters;
	private String apiReturnDesc;
	private String apiReturn;
	public String getApiDesc() {
		return apiDesc;
	}
	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}
	public String getApiParameters() {
		return apiParameters;
	}
	public void setApiParameters(String apiParameters) {
		this.apiParameters = apiParameters;
	}
	public String getApiReturnDesc() {
		return apiReturnDesc;
	}
	public void setApiReturnDesc(String apiReturnDesc) {
		this.apiReturnDesc = apiReturnDesc;
	}
	public String getApiReturn() {
		return apiReturn;
	}
	public void setApiReturn(String apiReturn) {
		this.apiReturn = apiReturn;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiDesc == null) ? 0 : apiDesc.hashCode());
		result = prime * result + ((apiParameters == null) ? 0 : apiParameters.hashCode());
		result = prime * result + ((apiReturn == null) ? 0 : apiReturn.hashCode());
		result = prime * result + ((apiReturnDesc == null) ? 0 : apiReturnDesc.hashCode());
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
		ApiDetails other = (ApiDetails) obj;
		if (apiDesc == null) {
			if (other.apiDesc != null)
				return false;
		} else if (!apiDesc.equals(other.apiDesc))
			return false;
		if (apiParameters == null) {
			if (other.apiParameters != null)
				return false;
		} else if (!apiParameters.equals(other.apiParameters))
			return false;
		if (apiReturn == null) {
			if (other.apiReturn != null)
				return false;
		} else if (!apiReturn.equals(other.apiReturn))
			return false;
		if (apiReturnDesc == null) {
			if (other.apiReturnDesc != null)
				return false;
		} else if (!apiReturnDesc.equals(other.apiReturnDesc))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ApiDetails [apiDesc=" + apiDesc + ", apiParameters=" + apiParameters + ", apiReturnDesc="
				+ apiReturnDesc + ", apiReturn=" + apiReturn + "]";
	}
	
}
