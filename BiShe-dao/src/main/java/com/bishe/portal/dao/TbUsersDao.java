package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.po.SimpleUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @return
     */
    TbUsers selectUserInfo(@Param("tel") String tel);

    /**
     *
     * @param tel
     * @return
     */
    TbUsers getUserInfoByTel(@Param("tel") String tel);

    /**
     *
     * @return
     */
    List<SimpleUserInfo> getAllAdminUserInfo();

    /**
     * @param ids
     * @return
     */
    List<SimpleUserInfo> selectUserInfoById (@Param("ids") List<Integer>ids);

    /**
     * @param userId
     * @return
     */
    TbUsers getUserInfoById(@Param("id") int userId);
}
