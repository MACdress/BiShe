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
     * 查询用户信息
     *
     * @param name
     * @return
     */
    TbUsers selectUserInfo(@Param("name") String name);
}
