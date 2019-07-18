package com.bishe.portal.service.utils;

import java.util.UUID;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 21:41
 * @Version 1.0
 **/
public class UUIDUtils {

    public static String getUUID(int length) {
        String uuid = UUID.randomUUID().toString().substring(0,length);
        return  uuid.replaceAll("-", "");
    }
}
