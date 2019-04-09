package com.bishe.portal.web.utils;

public class ReturnMessageInfo {
    private int code =0;
    private String message;
    private Object data;

    public String getSuccessMessage (Object object){
        code = 200;
        message = "成功";
        this.data = object;
    }
}
