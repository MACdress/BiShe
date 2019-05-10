package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamSelect;
import com.bishe.portal.model.vo.PageShowVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExamSelectDao {
    void insertExamSelectInfo(@Param("tbExamSelect") TbExamSelect tbExamSelect);

    List<TbExamSelect> getExamSelectByNumbers(@Param("subjectIdList") List<String> subjectIdList);

    TbExamSelect getExamSelectBySubject(@Param("subjectId") String subjectId);

    List<TbExamSelect> getAllSelectSubject(@Param("startIndex") int startIndex,@Param("pageSize")int pageSize);
}
