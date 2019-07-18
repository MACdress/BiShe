package com.bishe.portal.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEventVo implements Serializable {
    private String event;
    private String eventDate;
}
