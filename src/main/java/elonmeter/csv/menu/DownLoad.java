package elonmeter.csv.menu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;

public class DownLoad {
    /**  
     * @功能 下载临时素材接口  
     * @param filePath 文件将要保存的目录  
     * @param method 请求方法，包括POST和GET  
     * @param url 请求的路径  
     * @return  
     */  
      
	public static void saveUrlAs(String url,String localUrl,String method){  
	     //System.out.println("fileName---->"+filePath);  
	     //创建不同的文件夹目录  
	   /*  File file=new File(filePath);  
	     //判断文件夹是否存在  
	     if (!file.exists())  
	    {  
	        //如果文件夹不存在，则创建新的的文件夹  
	         file.mkdirs();  
	    }  */
	     FileOutputStream fileOut = null;  
	     HttpURLConnection conn = null;  
	     InputStream inputStream = null;  
	     try  
	    {  
	         // 建立链接  
	         URL httpUrl=new URL(url);  
	         conn=(HttpURLConnection) httpUrl.openConnection();  
	         //以Post方式提交表单，默认get方式  
	         conn.setRequestMethod(method);  
	         conn.setDoInput(true);    
	         conn.setDoOutput(true);  
	         // post方式不能使用缓存   
	         conn.setUseCaches(false);  
	         //连接指定的资源   
	         conn.connect();  
	         //获取网络输入流  
	         inputStream=conn.getInputStream();  
	         BufferedInputStream bis = new BufferedInputStream(inputStream);  
	         //判断文件的保存路径后面是否以/结尾  
	     /*    if (!filePath.endsWith("/")) {  
	  
	             filePath += "/";  
	  
	             }  */
	         //写入到文件（注意文件保存路径的后面一定要加上文件的名称）  
	         System.out.println(localUrl);
	         fileOut = new FileOutputStream(localUrl);  
	         BufferedOutputStream bos = new BufferedOutputStream(fileOut);  
	           
	         byte[] buf = new byte[4096];  
	         int length = bis.read(buf);  
	         //保存文件  
	         while(length != -1)  
	         {  
	             bos.write(buf, 0, length);  
	             length = bis.read(buf);  
	         }  
	         bos.close();  
	         bis.close();  
	         conn.disconnect();  
	    } catch (Exception e)  
	    {  
	         e.printStackTrace();  
	         System.out.println("抛出异常！！");  
	    }  
	       
	 } 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String photoUrl = "https://github.com/tyokyo/SIOEYE/blob/master/libs/commons-io-2.4.jar";                                      
       /* String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));   
        System.out.println("fileName---->"+fileName);  
        String localUrl = "d:/data"+fileName;    
        saveUrlAs(photoUrl, localUrl,"GET"); */
        URL url = new URL(photoUrl);
        FileUtils.copyURLToFile(url, new File("D:/data/commons-io-2.4.jar"), 20000, 30000);
	}

}
