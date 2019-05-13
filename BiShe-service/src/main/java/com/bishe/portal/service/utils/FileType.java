package com.bishe.portal.service.utils;

public enum FileType {
    JPEG("FFD8FF"),

    PNG("89504E47"),

    GIF("47494638");

    private String value = "";

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}