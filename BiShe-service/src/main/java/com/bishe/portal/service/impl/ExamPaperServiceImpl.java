package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbExamPaperDao;
import com.bishe.portal.model.mo.TbExamPaper;
import com.bishe.portal.model.po.FindExamPaperPo;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.service.utils.Encryption;
import com.bishe.portal.service.utils.UUIDUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 73515
 */
@Service
public class ExamPaperServiceImpl implements ExamPaperService {
    @Resource
    TbExamPaperDao tbExamPaperDao;
    @Override
    public void addExamPaper(ExamPaperVo examPaperVo) {
        TbExamPaper examPaper = getExamPaper(examPaperVo);
        examPaper.setExamPaperNum(UUIDUtils.getUUID(8));
        tbExamPaperDao.insertExamPaper(getExamPaper(examPaperVo));
    }

    @Override
    public void updateExamPaper(ExamPaperVo examPaperVo) {
        tbExamPaperDao.updateExamPaper(getExamPaper(examPaperVo));
    }

    @Override
    public void deleteExamPaper(String examPaperNum) {
        tbExamPaperDao.deleteExamPaper(examPaperNum);
    }

    @Override
    public List<ExamPaperVo> findExamPaperList(FindExamPaperVo findExamPaperVo) {
        List<TbExamPaper> examPapers = tbExamPaperDao.selectExamPaper(getExamPaperPo(findExamPaperVo));
        List<ExamPaperVo> result = new ArrayList<>();
        for (TbExamPaper examPaper:examPapers){
            ExamPaperVo examPaperVo = getExamPaperVo(examPaper);
            result.add(examPaperVo);
        }
        return result;
    }

    @Override
    public List<ExamPaperVo> getReleaseExamPaper() {
        List<ExamPaperVo> result = new ArrayList<>();
        List<TbExamPaper> examPapers = tbExamPaperDao.getReleaseExamPaper();
        if (examPapers.size() > 0) {
            for (TbExamPaper tbExamPaper : examPapers) {
                String invalidTime = tbExamPaper.getInvalidTime();
                Integer examTime = tbExamPaper.getExamTime();
                /* 验证是否可移除 */
                if (validateExamPaperBeOverdue(invalidTime,examTime)) {
                    /*
                      如果失效了，将已发布的考试变为未发布
                     */
                    tbExamPaperDao.updateExamStatus(tbExamPaper.getExamPaperNum());
                }else{
                    ExamPaperVo examPaperVo = getExamPaperVo(tbExamPaper);
                    result.add(examPaperVo);
                }
            }
        }
        return result;
    }

    private ExamPaperVo getExamPaperVo(TbExamPaper examPaper) {
        ExamPaperVo result = new ExamPaperVo();
        result.setCreateUnit(examPaper.getCreateUnit());
        result.setExamPaperName(examPaper.getExamPaperName());
        result.setInvalidTime(examPaper.getInvalidTime());
        result.setResponsible(examPaper.getResponsible());
        result.setStatus(examPaper.getStatus());
        result.setExamPaperType(examPaper.getExamPaperType());
        return result;
    }

    private FindExamPaperPo getExamPaperPo(FindExamPaperVo findExamPaperVo) {
        FindExamPaperPo result = new FindExamPaperPo();
        result.setCreateUnit(findExamPaperVo.getCreateUnit());
        result.setExamPaperName(findExamPaperVo.getExamPaperName());
        result.setInvalidBeginTime(findExamPaperVo.getInvalidBeginTime());
        result.setResponsible(findExamPaperVo.getResponsible());
        result.setInvalidEndTime(findExamPaperVo.getInvalidEndTime());
        return result;
    }

    private TbExamPaper getExamPaper(ExamPaperVo examPaperVo) {
        TbExamPaper result  = new TbExamPaper();
        result.setCreateUnit(examPaperVo.getCreateUnit());
        result.setExamPaperName(examPaperVo.getExamPaperName());
        result.setExamPaperType(examPaperVo.getExamPaperType());
        result.setInvalidTime(examPaperVo.getInvalidTime());
        result.setResponsible(examPaperVo.getResponsible());
        result.setStatus(examPaperVo.getStatus());
        result.setExamTime(examPaperVo.getExamTime());
        return result;
    }

    /**
     * 定时刷新考试安排记录，将过期考试变为待发布
     * 周一至周五 每隔15分钟刷新一次
     */
    @Scheduled(cron="0 */15 * * * MON-FRI")
    public void refreshExamPlan() {
        List<TbExamPaper> examPapers = tbExamPaperDao.getReleaseExamPaper();
        if (examPapers.size() > 0) {
            for (TbExamPaper tbExamPaper : examPapers) {
                String invalidTime = tbExamPaper.getInvalidTime();
                Integer examTime = tbExamPaper.getExamTime();
                /* 验证是否可移除 */
                if (validateExamPaperBeOverdue(invalidTime,examTime)) {
                    /*
                      如果失效了，将已发布的考试变为未发布
                     */
                    tbExamPaperDao.updateExamStatus(tbExamPaper.getExamPaperNum());
                }
            }
        }
    }
    private boolean validateExamPaperBeOverdue(String invalidTime, int examTime) {
        boolean flag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date beginTimeDate = sdf.parse(invalidTime);
            long beginTimeTime = beginTimeDate.getTime();

            /* 转换考试时间为毫秒单位 */
            int examTimeSecond = examTime * 60 * 1000;

            Date nowDate = new Date();
            long nowDateTime = nowDate.getTime();

            /* 当前时间超过了 考试结束时间，即为过期记录 */
            if(nowDateTime > (beginTimeTime+examTimeSecond)) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flag;
    }

}
