package sioeye.spider.entities;

import java.io.Serializable;

/**
 * @author jianbin.zhong
 *
 */
@SuppressWarnings("serial")
public class Scenes implements Serializable {
	private String sceneName;
	private String sceneUrl;
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public String getSceneUrl() {
		return sceneUrl;
	}
	public void setSceneUrl(String sceneUrl) {
		this.sceneUrl = sceneUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sceneName == null) ? 0 : sceneName.hashCode());
		result = prime * result + ((sceneUrl == null) ? 0 : sceneUrl.hashCode());
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
		Scenes other = (Scenes) obj;
		if (sceneName == null) {
			if (other.sceneName != null)
				return false;
		} else if (!sceneName.equals(other.sceneName))
			return false;
		if (sceneUrl == null) {
			if (other.sceneUrl != null)
				return false;
		} else if (!sceneUrl.equals(other.sceneUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Scenes [sceneName=" + sceneName + ", sceneUrl=" + sceneUrl + "]";
	}
	
	
}
