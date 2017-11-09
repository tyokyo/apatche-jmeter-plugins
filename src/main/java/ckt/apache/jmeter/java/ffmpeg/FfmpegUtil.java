package ckt.apache.jmeter.java.ffmpeg;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegUtil
{
	public static final Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);
	int exitValue = 1;

	public String[] Push(String ffmpeg, String video, String url)
	{
		String[] strarray = null;
		if (CheckFileType(video)) {
			StringBuffer stringBuffer = new StringBuffer();

			String osName = System.getProperty("os.name");
			logger.info("System=" + osName);
			if (osName.toLowerCase().indexOf("windows") > -1) {
				stringBuffer.append("cmd /c " + ffmpeg + "/ffmpeg -re ");
				stringBuffer.append("-i " + video + " -c copy -f flv ");
				stringBuffer.append(url);
				strarray = execcmd(stringBuffer.toString(), true);
			} else {
				stringBuffer.append(ffmpeg + "/ffmpeg -re ");
				stringBuffer.append("-i " + video + " -c copy -f flv ");
				stringBuffer.append(url);
				strarray = execcmd(stringBuffer.toString(), true);
			}
		} else {
			strarray = new String[] { "", "file type is not support!" };
		}

		return strarray;
	}
	public String[] PushPicture(String ffmpeg, String video, String url)
	{
		//ffmpeg -re -r 25 -loop 1 -i 1.jpg -c:v libx264 -t 3600 1.mp4
		String[] strarray = null;
		if (CheckFileType(video)) {
			StringBuffer stringBuffer = new StringBuffer();

			String osName = System.getProperty("os.name");
			logger.info("System=" + osName);
			if (osName.toLowerCase().indexOf("windows") > -1) {
				stringBuffer.append("cmd /c " + ffmpeg + "/ffmpeg -re ");
				stringBuffer.append("-r 25 -loop 1 -i " + video + " -c:v libx264 -t 9000 -f flv ");
				stringBuffer.append(url);
				strarray = execcmd(stringBuffer.toString(), true);
			} else {
				stringBuffer.append(ffmpeg + "/ffmpeg -re ");
				stringBuffer.append("-i " + video + " -c copy -f flv ");
				stringBuffer.append(url);
				strarray = execcmd(stringBuffer.toString(), true);
			}
		} else {
			strarray = new String[] { "", "file type is not support!" };
		}

		return strarray;
	}

	public int getExitValue() {
		return this.exitValue;
	}

	private boolean CheckFileType(String inputPath)
	{
		String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length()).toLowerCase();

		if (type.equals("avi"))
			return true;
		if (type.equals("mpg"))
			return true;
		if (type.equals("wmv"))
			return true;
		if (type.equals("3gp"))
			return true;
		if (type.equals("mov"))
			return true;

		if (type.equals("mp4"))
			return true;
		if (type.equals("asf"))
			return true;
		if (type.equals("asx"))
			return true;
		if (type.equals("flv")) {
			return true;
		}
		if (type.equals("jpg")) {
			return true;
		}
		if (type.equals("wmv9"))
			return false;
		if (type.equals("rm"))
			return false;
		if (type.equals("rmvb")) {
			return false;
		}
		return false;
	}

	public String[] execcmd(String commands, boolean wait) {
		String[] output = { "", "" };
		try
		{
			Process p = Runtime.getRuntime().exec(commands);
			logger.info("excute ok!" + commands);
			StreamCaptureThread errorStream = new StreamCaptureThread(p.getErrorStream(), "A");
			StreamCaptureThread outputStream = new StreamCaptureThread(p.getInputStream(), "B");

			new Thread(outputStream).start();
			new Thread(errorStream).start();
			if (wait) {
				p.waitFor();
			}
			String outputString = outputStream.output.toString();
			String errorString = errorStream.output.toString();
			output[0] = outputString;
			output[1] = errorString;
			this.exitValue = p.exitValue();
		} catch (InterruptedException|IOException e) {
			e.printStackTrace();
		}

		return output;
	}
}
