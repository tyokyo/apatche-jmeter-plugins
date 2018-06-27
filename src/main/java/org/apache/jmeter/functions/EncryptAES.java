package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/** 
* @ClassName: EncryptAES 
* @Description:使用AES加密解密 AES-128-ECB加密
* @author qiang.zhang@ck-telecom.com
* @date 2018年4月9日 下午2:39:31 
*  
*/
public class EncryptAES extends AbstractFunction {
	private static final List<String> desc = new LinkedList<String>();
	private static final String KEY = "__EncryptAES";
	private static final int MAX_PARAM_COUNT = 1;
	private static final int MIN_PARAM_COUNT = 1;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private Object[] values;
	
	// 加密
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original,"utf-8");
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	static {
		desc.add("aes-128-ecb DATA");
	}
	/**
	 * No-arg constructor.
	 */
	public EncryptAES() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String sSrc = new String(((CompoundVariable) values[0]).execute().trim());
		String sKey="sioeye2_rest2.0*";
		String encryptString="";;
		try {
			encryptString = Encrypt(sSrc, sKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("encryptString="+encryptString);
		return encryptString;
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
