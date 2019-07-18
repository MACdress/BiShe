package com.bishe.portal.service.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:27
 * @Version 1.0
 **/
public class Encryption {
    private static Logger logger = Logger.getLogger("logger");
    private static final String AESFORCBC = "AES/CBC/NoPadding";
    //获取盐值
    public static String getSale (){
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
       return sb.toString();
    }
    public static String getPwd(String password,String sale){
       return  md5Hex(password + sale);
    }
    public static String encodedByMD5(String source) throws IllegalAccessException {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte [] results = md.digest(source.getBytes());
            return bytesToHex(results);
        }catch (NoSuchAlgorithmException e){
            logger.info("进行Md5加密失败,{}"+e.getMessage());
        }
        return null;
    }
    public static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if ((src != null) && (src.length > 0)) {
            for (byte b : src) {
                int v = b & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString().toUpperCase();
        } else {
            return null;
        }
    }

    public static String encryptSHA1(String content){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(content.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append("0");
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            logger.info("进行Md5加密失败,{}"+e.getMessage());
        }
        return null;
    }

    /**
     * 使用AES的CBC模式加密
     * @param key 加密的秘钥
     * @param text 待加密的内容
     * @return 经过Base64编码的密文
     */
    public static String encryptByAESWithCBC(byte[] key,byte[] text){
        byte[] encrypt;
        SecretKeySpec keySpec;
        try{
            Cipher cipher = Cipher.getInstance(AESFORCBC);
            keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            encrypt = cipher.doFinal(text);
            return new org.apache.commons.codec.binary.Base64().encodeToString(encrypt);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param key 经Base64编码的AES秘钥
     * @param text 经Base64编码的加密串
     * @return
     */
    public static byte[] decryptByAESWithCBC(byte[] key,byte[] text){
        byte[] original = new byte[0];
        IvParameterSpec ivParameterSpec;
        try {
            Cipher cipher = Cipher.getInstance(AESFORCBC);
            ivParameterSpec = new IvParameterSpec(Arrays.copyOfRange(key, 0, 16));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            original = cipher.doFinal(text);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return original;
    }

}
