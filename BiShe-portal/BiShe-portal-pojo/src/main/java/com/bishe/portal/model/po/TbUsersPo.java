package com.bishe.portal.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:GaoPan
 * @Date:2018/12/19 22:18
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class TbUsersPo {
    private String pwd;
    private String email;
    private String name;
    /**
     * 用戶權限：0代表普通用戶，1代表管理者
     */
    private Integer permission;
    private String tel;
    private String userPhoto;
    private String birth;
    private String education;
    /**
     * 0代表女，1代表男
     */
    private Integer sex;

    private String sale;
}
