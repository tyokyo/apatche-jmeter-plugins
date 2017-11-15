package sioeye.spider.interfaces;

import java.util.List;

import sioeye.spider.entities.Scenes;

public interface ISceneSpider {
	
	/**
	 * 获取api doc scene list（包括scen和url）
	 * @param url
	 * @return
	 */
	public List<Scenes> getScene(String url);
}
