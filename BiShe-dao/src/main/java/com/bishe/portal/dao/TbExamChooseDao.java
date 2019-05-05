package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamChoose;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbExamChooseDao {
    TbExamChoose getExamChooseByNum(@Param("subjectId") String subjectId, @Param("examStartNum") String examStartNum);

    void insertExamChoose(@Param("examChoose") TbExamChoose examChoose);

    void updateAnswer(@Param("examStartNum") String examStartNum, @Param("subjectId") String subjectId,@Param("answer") String answer);

    List<TbExamChoose> getCorrectChoose(@Param("examStartNum") String examStartNum);
}
