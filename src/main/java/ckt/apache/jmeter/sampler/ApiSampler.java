package ckt.apache.jmeter.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class ApiSampler extends AbstractSampler {
	/** 
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */ 
	private static final long serialVersionUID = 1L;
	public final static String FUNCTION = "function"; 
	
	public static String METHOD = "Method";
	
	@Override
	public SampleResult sample(Entry arg0) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SampleResult res = new SampleResult();
		res.sampleStart();
		System.out.println("==========TestSampler================");
		System.out.println(this.getProperty(FUNCTION));//输出GUI界面所输入的函数方法返回结果
		res.sampleEnd();
		res.setSuccessful(true);
		return res;
	}
}