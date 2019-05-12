package com.bishe.portal.dao;

import com.bishe.portal.model.mo.TbExamSelectOption;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbExamSelectOptionDao {
     void insertExamSelectOption(@Param("tbExamSelectOption") TbExamSelectOption tbExamSelectOption);

    List<TbExamSelectOption> getSelectOptionList(@Param("examSelect") String id);
}
