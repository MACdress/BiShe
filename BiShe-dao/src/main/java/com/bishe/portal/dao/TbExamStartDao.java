package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamStart;
import org.apache.ibatis.annotations.Param;

public interface TbExamStartDao {
    TbExamStart getExamStartByNum(@Param("examStartNum") String examStartNum);

    void insertExamStart(@Param("examStart") TbExamStart examStart);

    void updateScore(@Param("score") int score, @Param("examStartNum") String examStartNum);
}
