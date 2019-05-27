package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbUserEvent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbUserEventDao {
    void insertEvent(@Param("event")TbUserEvent tbUserEvent);
    List<TbUserEvent> selectEventByUser(@Param("account")String account);
}
