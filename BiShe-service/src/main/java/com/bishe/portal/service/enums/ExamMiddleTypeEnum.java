package com.bishe.portal.service.enums;

public enum ExamMiddleTypeEnum {
    PARTY_CONSITUTION(1,"党章"),
    PARTY_RULES(2,"党规"),
    PARTY_HISTORY(3,"党史"),
    PARTY_BUILD(4,"党建"),
    QUALITY(5,"党员素质");


    private Integer status;
    private String  typeName;
    ExamMiddleTypeEnum(int status,String typeName){
        this.status = status;
        this.typeName = typeName;
    }
    public Integer getStatus(){
        return status;
    }
    public String getTypeName(){
        return typeName;
    }
    public static String getTypeNameStatus(int status){
        ExamMiddleTypeEnum[] values = ExamMiddleTypeEnum.values();
        for (ExamMiddleTypeEnum temp :values){
            if(temp.getStatus()==status){
                return temp.getTypeName();
            }
        }
        return "";
    }
}
