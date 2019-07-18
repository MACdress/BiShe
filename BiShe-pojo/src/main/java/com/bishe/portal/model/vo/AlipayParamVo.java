package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayParamVo implements Serializable {
    private String total_amount;
    private String body;
    private String timeout_express;
}
