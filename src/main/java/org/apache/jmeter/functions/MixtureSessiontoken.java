package org.apache.jmeter.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log.Logger;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;

public class MixtureSessiontoken extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__mixture_session_token";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;

	static {
		desc.add("mixture sessiontoken(get_session_token)");
	}
	public static String MD5(String sourceStr)
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
	public MixtureSessiontoken() {
		super();
	}
	private static String[] chars = {
     	"a", "b", "c", "d", "e", "f", "g", "h",
     	"i", "j", "k", "l", "m", "n", "o", "p",
     	"q", "r", "s", "t", "u", "v", "w", "x",
     	"y", "z", "0", "1", "2", "3", "4", "5",
     	"6", "7", "8", "9", "A", "B", "C", "D",
     	"E", "F", "G", "H", "I", "J", "K", "L",
     	"M", "N", "O", "P", "Q", "R", "S", "T",
     	"U", "V", "W", "X", "Y", "Z"};
	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String url = new String(((CompoundVariable) values[0]).execute().trim());
		String key = "sioeye";
		String  hex = MD5(key + url);
		String[] resUrl = new String[4];
		String outChars = "";
		for (int  i = 0; i < 4; i++) {
			//把加密字符按照8位一组16进制与0x3FFFFFFF进行位与运算 
			String subString = hex.substring(i*8, i*8+8);
			long subInt = Long.parseLong(subString, 16);
			long  hexint = 0x3FFFFFFF & subInt;
			System.out.println(hexint);
			for (int j = 0; j < 6; j++) {
				//把得到的值与0x0000003D进行位与运算，取得字符数组chars索引 
				int index = (int) (0x0000003D & hexint);
				//把取得的字符相加 
				outChars += chars[index];
				//每次循环按位右移5位 
				hexint = hexint >> 5;
			}
			resUrl[i] = outChars;
		}
		String mixtureSessiontokenString = resUrl[0];
		log.info(String.format("sessiontoken=%s to mixture result is %s ", url,mixtureSessiontokenString));
		return mixtureSessiontokenString;
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
