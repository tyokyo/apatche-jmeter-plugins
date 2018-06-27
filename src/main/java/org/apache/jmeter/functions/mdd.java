package org.apache.jmeter.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class mdd {

	public mdd() {
		// TODO Auto-generated constructor stub
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
	public static void main(String[] args) {

		String a="e1226e12"; 
		long b = Long.parseLong(a, 16);

		/*String hex = Long.toHexString(b);
		System.out.println(hex);*/

		try {
			String aen = Encrypt("13547946743", "sioeye2_rest1.0*");
			System.out.println(aen);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = "988d0c4f61b665a8febd4a52575fea97";
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
		System.out.println(resUrl[0]);
	}

}
