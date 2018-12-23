package com.bishe.portal.model.mo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author 熊猫
 */
@Data
@NoArgsConstructor
public class TbUsers {
    @NonNull
    private long userId;
    private String pwd;
    private String email;
    private String name;
    /**
     * 用戶權限：0代表普通用戶，1代表管理者
     */
    private int permission;
    private String tel;
    private String userPhoto;
    private String birthDay;
    private String education;
    /**
     * 0代表女，1代表男
     */
    private int sex;

    private String sale;

}
