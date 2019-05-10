package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamJudge;
import com.bishe.portal.model.vo.PageShowVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExamJudgeDao {
    void insertExamJudgeInfo(@Param("tbExamJudge") TbExamJudge tbExamJudge);

    List<TbExamJudge> getExamJudgeByNumbers(@Param("subjectIdList") List<String> subjectIdList);

    TbExamJudge getExamJudgeBySubject(@Param("subjectId") String subjectId);

    List<TbExamJudge> getAllJudgeSubject(@Param("startIndex") int startIndex,@Param("pageSize")int pageSize);
}
