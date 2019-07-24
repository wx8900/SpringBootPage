package com.demo.test.security;

import lombok.Builder;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 1、公钥加密，私钥解密用于信息加密
 * 2、私钥加密，公钥解密用于数字签名
 *
 * @author Jack
 * @date 2019/07/23  02:49
 */
@Builder
public class RSAKey {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";
    private static Logger logger = LogManager.getLogger(RSAKey.class);
    private static RSAPublicKey rsaPublicKey;
    private static RSAPrivateKey rsaPrivateKey;

    public RSAKey () {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("Init Public Key is " + rsaPublicKey);
            System.out.println("Init Private Key is " + rsaPrivateKey);
            System.out.println();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    /**
     * 公钥加密、私钥解密——加密
     *
     * @param src
     * @return
     */
    public String pubEncode(String src) {
        String res = "";
        try {
            /*X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);*/
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            byte[] result = cipher.doFinal(src.getBytes(CHARSET));
            res = Base64.encodeBase64String(result);
            System.out.println("公钥加密、私钥解密——加密 : " + Base64.encodeBase64String(result));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    /**
     * 公钥加密、私钥解密——解密
     * Note: 取到的rsaPrivateKey一直为空，程序报错 No installed provider supports this key: (null)
     * 所以key使用String类型
     *
     * @param src
     * @return
     */
    public String priDecode(String src) {
        String res = "";
        try {
            /*PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);*/
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] result = cipher.doFinal(src.getBytes());
            res = result.toString();
            System.out.println("公钥加密、私钥解密——解密：" + res);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        //writeToFile("RSA/publicKey", getRsaPublicKey().getBytes());
        //writeToFile("RSA/privateKey", getRsaPrivateKey().getBytes());

        RSAKey rsaKey  = new RSAKey();
        String password = "12345678";
        String passwordStr = rsaKey.pubEncode(password);
        System.out.println("password 加密之后：" + passwordStr);
        System.out.println("password 解密之后：" + rsaKey.priDecode(passwordStr));
    }
}
