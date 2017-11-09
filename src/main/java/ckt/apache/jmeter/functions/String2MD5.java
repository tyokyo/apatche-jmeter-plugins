package ckt.apache.jmeter.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

public class String2MD5 extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__String2MD5";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	//private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("Convert String to MD5");
	}
	private  String MD5(String sourceStr)
	{
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte[] b = md.digest();

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				int i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
		}
		catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}
	/**
	 * No-arg constructor.
	 */
	public String2MD5() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String str2md5 = new String(((CompoundVariable) values[0]).execute().trim());
		String md5String = MD5(str2md5);
		return md5String;
	}

	/** {@inheritDoc} */
	@Override
	public String getReferenceKey() {
		return KEY;
	}

	@Override
	public List<String> getArgumentDesc() {
		return desc;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		checkParameterCount(parameters, MIN_PARAM_COUNT, MAX_PARAM_COUNT);
		values = parameters.toArray();
	}
}
