package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbQuestionnaireAnswer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TbQuestionnaireAnswerDao {
    void insertAnswer(@Param("tbQuestionnaireAnswer") TbQuestionnaireAnswer tbQuestionnaireAnswer);

    TbQuestionnaireAnswer selectAnswerById(@Param("questionId") String questionId,@Param("account") String account);

    TbQuestionnaireAnswer selectAnswerByNum(@Param("questionnaireNum") String questionnaireNum,@Param("account") String account);
}
