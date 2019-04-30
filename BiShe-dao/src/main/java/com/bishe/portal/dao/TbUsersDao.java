package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbUsers;
import com.bishe.portal.model.vo.SelectUserParamVo;
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
     * @param tbUsers 用户信息
     */
    void insert(@Param("tbUsers") TbUsers tbUsers);

    /**
     *根据账号查询用户信息
     * @param account 账号
     * @return 返回用户信息
     */
    TbUsers getUserInfoByAccount(@Param("account") String account);

    /**
     *获取用户信息
     * @return 返回符合条件的用户信息
     */
    List<TbUsers> getAllUserInfo(@Param("selectParam") SelectUserParamVo paramVo);

    /**
     * 更据userId获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    TbUsers getUserInfoById(@Param("id") int userId);

    /**
     * 更具电话号码获取用户信息
     * @param tel 电话号码
     * @return 用户信息
     */
    TbUsers getUserInfoByTel(@Param("tel") String tel);
}
