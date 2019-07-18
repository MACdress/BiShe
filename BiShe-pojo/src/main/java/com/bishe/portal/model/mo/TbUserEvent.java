package com.bishe.portal.model.mo;

import lombok.Data;

import java.util.Date;

@Data
public class TbUserEvent {
    private int id;
    private Date eventDate;
    private String event;
    private String account;
}
