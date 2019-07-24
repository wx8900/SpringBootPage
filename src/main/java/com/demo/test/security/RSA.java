package com.demo.test.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Jack
 * @date 2019/07/23  02:49
 */
public class RSA {

    private static Logger logger = LogManager.getLogger(RSA.class);
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";

    private static String rsaPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJcBeOb97IdkkirBmx3MOY5e4eRwh0uvC2BcNlY1rDo0lZ8ibR1bl1RJXWkHv7U0ASO/5DBlnnnGbQRtsJlsCPMCAwEAAQ==";
    private static String rsaPrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAlwF45v3sh2SSKsGbHcw5jl7h5HCHS68LYFw2VjWsOjSVnyJtHVuXVEldaQe/tTQBI7/kMGWeecZtBG2wmWwI8wIDAQABAkAdSkPRSl+ew3s2n+cemIZxfyYB0XHs1D84qapAfpixkUNvWL0A4ovrwsnwt4MEjAtWVTufNvTxIZcZdx+Q5DbBAiEA9TzzYMGRU+3mdlAx0ICF+NIqwvlqyvedKa4KSx55gVUCIQCdoeX6mqGRP78aQjYKWeogwliszjU5fN/LFvKZrcgBJwIhAMvbBLzzaykHY0IKW75kd/lkSyOUTY+20bAp+miDRqGZAiA6r36eeRkzqUbtcL8LxYPb5F79HtxD5dCvnIB/ZGp0uwIgWtXI7IxHjYCsNomSJdu1J3dU9KqQuW/eOHxrk/OgUYE=";

    public static void initKey() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            rsaPublicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            rsaPrivateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
            System.out.println("Public Key is "+ rsaPublicKey);
            System.out.println("Private Key is "+ rsaPrivateKey);
            System.out.println();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    /**
     * 解码PublicKey
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = java.util.Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解码PrivateKey
     * @param key
     * @return
     */
    public static PrivateKey  getPrivateKey(String key) {
        try {
            byte[] byteKey = java.util.Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec x509EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param src
     * @return
     */
    public static String pubEncode(String src) {
        String res = "";
        try {
            Cipher encryptCipher = Cipher.getInstance(RSA_ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, getPublicKey(rsaPublicKey));
            byte[] cipherText = encryptCipher.doFinal(src.getBytes(CHARSET));
            res = java.util.Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    public static String priDecode(String cipherText) {
        String res = "";
        try {
            byte[] bytes = java.util.Base64.getDecoder().decode(cipherText);
            Cipher decriptCipher = Cipher.getInstance(RSA_ALGORITHM);
            decriptCipher.init(Cipher.DECRYPT_MODE, getPrivateKey(rsaPrivateKey));
            res = new String(decriptCipher.doFinal(bytes), CHARSET);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        String pwd = "12345678" + System.currentTimeMillis();  //随机串
        String passwordStr = pubEncode(pwd);
        System.out.println("11111passwordStr is "+passwordStr);
        System.out.println("22222decrypt is " + priDecode(passwordStr));
    }
}
