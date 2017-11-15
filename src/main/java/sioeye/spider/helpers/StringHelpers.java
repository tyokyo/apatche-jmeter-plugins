package sioeye.spider.helpers;

public class StringHelpers {
	/**
	 * 将./url替换为/url
	 * @param url   ./classes/appstartupmanage.html
	 * @return  /classes/appstartupmanage.html
	 */
	public String getDealString(String url){
		String dealString=null;
		dealString=url.replace("./","");
		return dealString;
	}
}
