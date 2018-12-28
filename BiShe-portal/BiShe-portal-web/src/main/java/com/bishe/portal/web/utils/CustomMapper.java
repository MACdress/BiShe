package com.bishe.portal.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * @author 熊猫
 */
public class CustomMapper extends ObjectMapper{;
    public CustomMapper() {
        this.setSerializationInclusion(Include.NON_NULL);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
