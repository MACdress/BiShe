package com.bishe.portal.service.enums;

public enum ExamPaperStatusEnum {
    /*
        考试未开始
     */
    EXAM_PAPER_UN_BEGIN(2),
    /**
     * 考试已结束
     */
    EXAM_PAPER_IS_END(3),
    /**
     * 未考试
     */
    EXAM_UN_START(0),
    /**
     * 以考试
     */
    EXAM_IS_START(1);


    private Integer status;
    ExamPaperStatusEnum(int status){
        this.status = status;
    }
    public Integer getStatus(){
        return status;
    }
}
