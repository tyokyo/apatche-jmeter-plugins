package org.apache.jmeter.sampler;

import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.samplers.AbstractSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jmeter.samplers.Entry;

public class FFmpegSampler extends AbstractSampler {
	private static final long serialVersionUID = 1L;
	public final static String ffpmeg = "FFmpegSampler.ffmpeg.location"; 
	public final static String video_path = "FFmpegSampler.video.location";
	public final static String pushurl = "FFmpegSampler.video.push.url";
	private static Logger log = LoggerFactory.getLogger(FFmpegSampler.class);
	
	public String  getffpmeg() {
		return getPropertyAsString(ffpmeg);
	}
	public void setffpmeg(String _ffmpeg_path) {
		setProperty(ffpmeg, _ffmpeg_path);
	}
	public String  getVideoPath() {
		return getPropertyAsString(video_path);
	}
	public void setVideoPath(String _video_path) {
		setProperty(video_path, _video_path);
	}
	public String  getVideoPushUrl() {
		return getPropertyAsString(pushurl);
	}
	public void setVideoPushUrl(String _push_url) {
		setProperty(pushurl, _push_url);
	}
	@Override
	public HTTPSampleResult sample(Entry arg0) {
		HTTPSampleResult res = new HTTPSampleResult();
		res.sampleStart();
		/*String url=String.format("https://%s%s", getServer(),getMethod());*/
		res.setHTTPMethod("POST");
		
		return res;
	}
}