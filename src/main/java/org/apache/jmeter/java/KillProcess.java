package org.apache.jmeter.java;

/* 
 * 一般来说，扩展java请求通常需要的类有： 
 * AbstractJavaSamplerClient 
 * JavaSamplerContext 容器类 
 * SampleResult 结果统计类 
 * Argument 参数类 
 * */ 
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient; 
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext; 
import org.apache.jmeter.samplers.SampleResult; 
import org.apache.jmeter.config.Arguments; 
public class KillProcess extends AbstractJavaSamplerClient { 
	private String  agt1;
	private String resultData; 
	/* 
	 * 通过getDefaultParameters()可以来定义入参 
	 * 
	 * */
	public Arguments getDefaultParameters(){
		//定义属性对象，用来添加GUI参数
		Arguments params = new Arguments();
		//添加一个GUI参数，名称叫"method"，值是"killAllProcess"
		params.addArgument("processName","firefox.exe");
		return params;
	}
	//每个线程测试前执行一次，做一些初始化工作；
	public void setupTest(JavaSamplerContext arg0) {
		System.out.println("start to kill process");
	}

	//JavaSampleContext arg0()就是用来接收GUI所提交的
	@Override
	public  SampleResult runTest(JavaSamplerContext arg0) {
		// TODO Auto-generated method stub
		SampleResult sr = new SampleResult();
		try{
			//是表示请求开始计时
			sr.sampleStart();
			agt1 = arg0.getParameter("processName",null);
			try {
				WindowsCommandUtil.killTask(agt1);
				resultData=String.format("kill process named %s  success",agt1);
			} catch (Exception e) {
				// TODO: handle exception
				resultData=String.format("there has no  process named %s",agt1);
			}
			if(resultData != null && resultData.length()>0){
				//设置响应数据为resutlData;也可以用sr.setSamplerData("OK");
				sr.setResponseData(resultData,null);
				//设置响应代码为0
				sr.setResponseCode("0000");
				//设置响应类型为text
				sr.setContentType(SampleResult.TEXT);
				//设置响应状态为true
				sr.setSuccessful(true);
				//设置响应信息
				sr.setResponseMessage("方法执行成功！");
			}
			else{
				sr.setResponseData(resultData,null);
				//设置响应代码为-9999
				sr.setResponseCode("-9999");
				//设置响应类型为txt
				sr.setContentType(SampleResult.TEXT);
				//设置响应状态为true
				sr.setSuccessful(false);
				//设置响应信息
				sr.setResponseMessage("方法执行失败！");
			}   

		}
		catch(Exception e){
			sr.setResponseData(e.getMessage(),null);
			e.printStackTrace();
			sr.setResponseCode("-1");
			sr.setContentType(SampleResult.TEXT);
			sr.setSuccessful(false);
			sr.setResponseMessage("捕获异常！");
		}
		finally{
			//表示请求结束计时
			sr.sampleEnd();
		}

		//表示返回这个SampleResult对象sr
		return sr;
	}
	public void teardownTest(JavaSamplerContext arg0) {
		System.out.println("end to kill process");
		// System.out.println("The cost is"+(end-start)/1000);
	}
}