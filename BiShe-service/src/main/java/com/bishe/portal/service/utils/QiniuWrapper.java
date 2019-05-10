package com.bishe.portal.service.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class QiniuWrapper {
    private static final Logger logger = LoggerFactory.getLogger(QiniuWrapper.class);

    private static final String CONFIG_BUCKET="bishe";

    private static final String CONFIG_AK="11slZv0HATU2UjlNolz73rEMTRKVrhV7jbeTosuy";
    private static final String CONFIG_SK="OocSIZ1hWqmcq4h3T0eCK00uR1RPYok16BZZiNMR";
    private static final String CONFIG_CDN="qiniu.cdns";

    private static final Auth auth;
    private static final UploadManager uploadManager;

    private static final String bucketName;
    private static final List<String> cdns;

    /**
     * 从外部文件中初始化七牛存储相关的配置信息
     */
    static{
       // Properties properties =PropertiesUtil.getDefaultProperties();
        auth = Auth.create(CONFIG_AK, CONFIG_SK);
        Configuration cfg = new Configuration(Zone.zone2());//设置空间上传域名
        uploadManager = new UploadManager(cfg);
        bucketName=CONFIG_BUCKET;
        String cdn = CONFIG_CDN;
        cdns = Arrays.asList(cdn.split(","));
    }

    /**
     * 获取上传资源的token
     * @return
     */
    public static String getUploadToken(){
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取更新资源的token，只能用于更新参数key所代表的资源
     * @param key 存储空间中已存在的资源key
     * @return
     */
    public static String getUploadToken(String key){
        return auth.uploadToken(bucketName,key);
    }

    /**
     * 上传文件
     * @param data 二进制格式的文件内容
     * @param key 文件在七牛中的key
     * @param update 是否是更新
     * @return
     */
    public static String upload(byte[] data,String key,boolean update){
        try {
            String token = update?auth.uploadToken(bucketName,key):auth.uploadToken(bucketName);
            Response response = uploadManager.put(data, getFullKey(data, key), token);
            DefaultPutRet ret = response.jsonToObject(DefaultPutRet.class);
            return ret.key;
        } catch (QiniuException e) {
            logger.error("123",e);
        }
        return null;
    }

    private static String getFullKey(byte[] data,String key){
        FileType type= FileTypeHelper.getType(data);
        if(type!=null){
            return key+"."+type.name().toLowerCase();
        }else{
            return key;
        }
    }

    public static String getUrl(String key,String model){
        return getUrl(key, model, 3600);
    }

    /**
     * 获取多个key图片；
     * @param keys 逗号隔开的多个key;
     * @param model
     * @return
     */
    public static List<String> getUrls(String keys,String model){
        List<String> list = null;
        if (!StringUtils.isEmpty(keys)&&keys.trim().length()>0) {
            list = new ArrayList<>();
            for (String key : keys.split(",")) {
                list.add(getUrl(key, model, 3600));
            }
        }
        return list;
    }

    public static String getUrl(String key){
        if(!StringUtils.isEmpty(key)){
            return auth.privateDownloadUrl("http://"+getCDN()+"/@"+key);
        }
        return null;
    }

    /**
     * @param key
     * @param expires 单位：秒
     * @return
     */
    public static String getUrl(String key,long expires){
        if(!StringUtils.isEmpty(key)){
            long time = System.currentTimeMillis()/1000+expires;
            return auth.privateDownloadUrl("http://"+getCDN()+"/@"+key,time);
        }
        return null;
    }

    public static String getUrl(String key,String model,long expires){
        if(StringUtils.hasText(model)){
            return auth.privateDownloadUrl("http://"+getCDN()+"/@"+key+"?"+model,expires);
        }else{
            return auth.privateDownloadUrl("http://"+getCDN()+"/@"+key,expires);
        }
    }

    /**
     * 从多条CDN路径中随机选择一条
     * @return
     */
    private static String getCDN(){
        Random random= new Random();
        int num=random.nextInt(cdns.size());
        return cdns.get(num);
    }
}
