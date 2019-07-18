package com.bishe.portal.service.utils;

import java.text.MessageFormat;

public  class QiniuKeyGenerator {
    public static final String KEY = "/{0}/{1}/{2}/{3}";// 多图片可以按照：/表名/字段名/业务值(refId)/时间戳 处理

    public static String generateKey(){
        return MessageFormat.format(KEY, "default", "all", "0", UUIDUtils.getUUID(8));
    }

}
