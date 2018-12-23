package com.bishe.portal.service.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:27
 * @Version 1.0
 **/
public class Encryption {
    private static int length =48;
    /**
     * 获取盐值
     * @return
     */
    public static String getSale(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

    /**
     * 利用MD5+盐进行加密
     * @return
     */
    public static String getPwd(String sale,String pwd){
        if ((pwd != null) && (sale != null)) {
            String password = md5Hex(pwd + sale);
            char[] cs = new char[length];
            for (int i = 0; i < length; i += 3) {
                cs[i] = password.charAt(i / 3 * 2);
                char c = sale.charAt(i / 3);
                cs[i + 1] = c;
                cs[i + 2] = password.charAt(i / 3 * 2 + 1);
            }
            return String.valueOf(cs);
        } else {
            return "";
        }

    }
    @SuppressWarnings("unused")
    private static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes());
            return new String(new Hex().encode(digest));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "";
        }
    }

}
