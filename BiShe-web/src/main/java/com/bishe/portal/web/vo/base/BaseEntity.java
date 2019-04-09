package com.bishe.portal.web.vo.base;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人(username)
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;
}
