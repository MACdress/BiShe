package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamPaperMiddle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExamPaperMiddleDao {
    void insertTbExamPaperMiddle(@Param("tbExamPaperMiddle") TbExamPaperMiddle tbExamPaperMiddle);

    List<String> getSubjectIdByExamPaperNum(@Param("examPaperNum") String examPaperNum);

    void deleteExamPaperSubject(@Param("examPaperNum") String examPaperNum,@Param("subjectIdList") List<String> subjectId);
}
