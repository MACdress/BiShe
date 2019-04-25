package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamPaper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 73515
 */
public interface TbExamPaperDao {
    /**
     * 新增一张试卷
     * @param examPaper 试卷基本信息
     */
    void insertExamPaper(@Param("examPaper") TbExamPaper examPaper);

    /**
     * 更新一张试卷
     * @param examPaper 试卷基本信息
     */
    void updateExamPaper(@Param("examPaper")TbExamPaper examPaper);

    void deleteExamPaper(@Param("id") int examPaperId);
}
