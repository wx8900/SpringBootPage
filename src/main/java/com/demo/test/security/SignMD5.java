package com.demo.test.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Signature by MD5
 *
 * @author Jack
 * @date 2019/07/24  02:43
 */

public class SignMD5 {

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

    public static void main(String[] args) {
        String str = "money=100&phone=4086591234";
        System.out.println(getMD5(str));
    }
}
