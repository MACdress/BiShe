package com.bishe.portal.model.mo;

import lombok.Data;

/**
 * @Author:GaoPan
 * @Date:2018/12/27 20:59
 * @Version 1.0
 **/
@Data
public class TbUserCollections {
    private int id;
    private int userNum;
    private int classify;
    private int objectId;
    /**
     * 用户收藏备注
     */
    private String tips;
}
