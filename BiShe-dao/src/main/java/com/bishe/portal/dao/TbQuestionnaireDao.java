package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbQuestionnaire;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbQuestionnaireDao {
    void insertQuestionnaire(@Param("tbQuestionnaire") TbQuestionnaire tbQuestionnaire);

    TbQuestionnaire selectQuestionnaireByNum(@Param("questionnaireNum") String questionnaireNum);

    void deleteQuestionnaire(@Param("questionnaireNum") String questionnaireNum);

    List<TbQuestionnaire> getAllQuestionnaire();
}
