package sioeye.spider.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApiParameters implements Serializable {
	private String apiParameterName;
	private String apiParameterType;
	private String apiParameterFlag;
	private String apiParameterDesc;
	public String getApiParameterName() {
		return apiParameterName;
	}
	public void setApiParameterName(String apiParameterName) {
		this.apiParameterName = apiParameterName;
	}
	public String getApiParameterType() {
		return apiParameterType;
	}
	public void setApiParameterType(String apiParameterType) {
		this.apiParameterType = apiParameterType;
	}
	public String getApiParameterFlag() {
		return apiParameterFlag;
	}
	public void setApiParameterFlag(String apiParameterFlag) {
		this.apiParameterFlag = apiParameterFlag;
	}
	public String getApiParameterDesc() {
		return apiParameterDesc;
	}
	public void setApiParameterDesc(String apiParameterDesc) {
		this.apiParameterDesc = apiParameterDesc;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiParameterDesc == null) ? 0 : apiParameterDesc.hashCode());
		result = prime * result + ((apiParameterFlag == null) ? 0 : apiParameterFlag.hashCode());
		result = prime * result + ((apiParameterName == null) ? 0 : apiParameterName.hashCode());
		result = prime * result + ((apiParameterType == null) ? 0 : apiParameterType.hashCode());
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
		ApiParameters other = (ApiParameters) obj;
		if (apiParameterDesc == null) {
			if (other.apiParameterDesc != null)
				return false;
		} else if (!apiParameterDesc.equals(other.apiParameterDesc))
			return false;
		if (apiParameterFlag == null) {
			if (other.apiParameterFlag != null)
				return false;
		} else if (!apiParameterFlag.equals(other.apiParameterFlag))
			return false;
		if (apiParameterName == null) {
			if (other.apiParameterName != null)
				return false;
		} else if (!apiParameterName.equals(other.apiParameterName))
			return false;
		if (apiParameterType == null) {
			if (other.apiParameterType != null)
				return false;
		} else if (!apiParameterType.equals(other.apiParameterType))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ApiParameters [apiParameterName=" + apiParameterName + ", apiParameterType=" + apiParameterType
				+ ", apiParameterFlag=" + apiParameterFlag + ", apiParameterDesc=" + apiParameterDesc + "]";
	}
	
}
