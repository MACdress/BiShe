package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamPaper;
import com.bishe.portal.model.po.FindExamPaperPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 删除一张试卷
     * @param examPaperId 试卷的ID
     */
    void deleteExamPaper(@Param("id") int examPaperId);

    /**
     * 获取试卷基础信息
     * @param examPaperPo 查询的字段
     * @return 试卷的基础信息集合
     */
    List<TbExamPaper> selectExamPaper(@Param("examPaper") FindExamPaperPo examPaperPo);
}
