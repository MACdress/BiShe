package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbQuestionnaireSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbQuestionnaireSelectDao {
    void insertQuestionnaireSelect(@Param("tbQuestionnaireSelect") TbQuestionnaireSelect tbQuestionnaireSelect);

    TbQuestionnaireSelect getQuestionnaireSelectById(@Param("questionId") String questionId);

    void deleteQuestion(@Param("questionIdList") List<String> questionIdList);

    List<TbQuestionnaireSelect> getQuestionnaireSelectByNum(@Param("questionnaireNum") String questionnaireNum, @Param("startIndex") int startIndex, @Param("pageSize")int pageSize);

    void deleteQuestionByNum(@Param("questionnaireNum") String questionnaireNum);
}
