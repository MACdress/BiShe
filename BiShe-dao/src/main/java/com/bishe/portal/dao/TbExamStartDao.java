package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamStart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExamStartDao {
    TbExamStart getExamStartByNum(@Param("examStartNum") String examStartNum);

    void insertExamStart(@Param("examStart") TbExamStart examStart);

    void updateScore(@Param("score") int score, @Param("examStartNum") String examStartNum);

    List<TbExamStart> getFinishExamInfo(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize, @Param("examPaperNum") String examPaperNum);

    List<TbExamStart> getExamStartByExaminee(@Param("account") String account);

    List<TbExamStart> getFinishExamStartByExaminee(@Param("account") String account);
}
