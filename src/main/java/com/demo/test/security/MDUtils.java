package com.demo.test.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author Jack
 * @date 2019/05/30  14:50:12
 */
public class MDUtils {

    static Logger logger = LogManager.getLogger(MDUtils.class);

    public static void main(String[] args) {
        String str = "money=100&phone=4086591234";
        System.out.println(getMD5(str));
    }

    public static String getMD5(String str) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加密
     *
     * @param origin      要被加密的字符串
     * @param charsetname 加密字符,如UTF-8
     * @date 2019/05/30  14:50:12
     */
    public static String MD5EncodeForHex(String origin, String charsetname)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return MD5EncodeForHex(origin.getBytes(charsetname));
    }

    public static String MD5EncodeForHex(byte[] origin) throws NoSuchAlgorithmException {
        return Hex.encodeHexString(digest("MD5", origin));
    }

    /**
     * 指定加密算法
     *
     * @throws NoSuchAlgorithmException
     * @date 2019/05/30  14:50:12
     */
    private static byte[] digest(String algorithm, byte[] source) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance(algorithm);
        return md.digest(source);
    }

    public static String encrypt(String toString) {
        return "";
    }
}