package ro.mobilPay.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static String hash(String s) throws NoSuchAlgorithmException {
		MessageDigest m=MessageDigest.getInstance("MD5");
		//String sRet = ""+System.currentTimeMillis();
		m.update(s.getBytes(),0,s.length());
		return new BigInteger(1,m.digest()).toString(16);
		
	}

}
