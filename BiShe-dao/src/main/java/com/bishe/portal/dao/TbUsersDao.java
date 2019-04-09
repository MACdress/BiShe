package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbUsers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author:GaoPan
 * @Date:2018/12/20 19:23
 * @Version 1.0
 **/
@Repository
public interface TbUsersDao {
    /**
     * 插入用户信息
     *
     * @param tbUsers
     * @return
     */
    int insert(@Param("tbUsers") TbUsers tbUsers);

    /**
     *
     * @param tel
     * @param permission
     * @return
     */
    TbUsers selectUserInfo(@Param("tel") String tel,@Param("permission") int permission);

    /**
     *
     * @param tel
     * @return
     */
    TbUsers getUserInfoByTel(@Param("tel") String tel);
}
