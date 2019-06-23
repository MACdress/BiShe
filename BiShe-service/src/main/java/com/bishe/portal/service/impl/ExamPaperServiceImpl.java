package com.bishe.portal.service.impl;

import com.bishe.portal.dao.TbExamPaperDao;
import com.bishe.portal.dao.TbExamStartDao;
import com.bishe.portal.model.mo.TbExamPaper;
import com.bishe.portal.model.mo.TbExamStart;
import com.bishe.portal.model.po.FindExamPaperPo;
import com.bishe.portal.model.vo.ExamPaperVo;
import com.bishe.portal.model.vo.FindExamPaperVo;
import com.bishe.portal.service.ExamPaperService;
import com.bishe.portal.service.enums.ExamPaperStatusEnum;
import com.bishe.portal.service.utils.COSClientUtil;
import com.bishe.portal.service.utils.RedisCacheUtil;
import com.bishe.portal.service.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 73515
 */
@Service
public class ExamPaperServiceImpl implements ExamPaperService {
    @Resource
    TbExamPaperDao tbExamPaperDao;
    @Resource
    TbExamStartDao tbExamStartDao;
    @Resource
    RedisCacheUtil redisCacheUtil;
    private static final Logger logger = LoggerFactory.getLogger(ExamPaperServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamPaperVo addExamPaper(ExamPaperVo examPaperVo) {
        ExamPaperVo examPaperVo1 = new ExamPaperVo();
        try {
            String imgUrl = "";
            if (examPaperVo.getExamPaperImg() != null) {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String name = cosClientUtil.uploadFile2Cos(examPaperVo.getExamPaperImg());
                imgUrl = cosClientUtil.getImgUrl(name);
            }
            TbExamPaper examPaper = getExamPaper(examPaperVo);
            examPaper.setExamPaperNum(UUIDUtils.getUUID(8));
            examPaper.setExamPaperImg(imgUrl);
            System.out.println("插入前"+examPaper);
            tbExamPaperDao.insertExamPaper(examPaper);
            System.out.println("插入后"+examPaper);
            TbExamPaper examPaperByNumber = tbExamPaperDao.getExamPaperByNumber(examPaper.getExamPaperNum());
            System.out.println(examPaperByNumber);
            examPaperVo1 = getExamPaperVo(examPaperByNumber);
            System.out.println("---------------"+examPaperVo1);
            return examPaperVo1;
        }catch (Exception e){
            e.printStackTrace();
            return examPaperVo1;
        }finally {
            return examPaperVo1;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExamPaper(ExamPaperVo examPaperVo) {
        try {
            String imgUrl = "";
            if (examPaperVo.getExamPaperImg() != null) {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String name = cosClientUtil.uploadFile2Cos(examPaperVo.getExamPaperImg());
                imgUrl = cosClientUtil.getImgUrl(name);
            }
            TbExamPaper examPaper = getExamPaper(examPaperVo);
            examPaper.setExamPaperImg(imgUrl);
            tbExamPaperDao.updateExamPaper(examPaper);
        }catch (Exception e){
            logger.info("更新试卷信息异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public List<ExamPaperVo> getReleaseExamPaper(String account) {
        List<ExamPaperVo> result = new ArrayList<>();
        List<TbExamPaper> examPapers = tbExamPaperDao.getReleaseExamPaper();
        if (examPapers.size() > 0) {
            List<TbExamStart> examStartList = tbExamStartDao.getExamStartByExaminee(account);
            Map<String,TbExamStart> examStartMap = new HashMap<>();
            for (TbExamStart tbExamStart:examStartList){
                examStartMap.put(tbExamStart.getExamPaperNum(),tbExamStart);
            }
            for (TbExamPaper tbExamPaper : examPapers) {
                String invalidTime = tbExamPaper.getInvalidTime();
                Integer examTime = tbExamPaper.getExamTime();
                ExamPaperVo examPaperVo = getExamPaperVo(tbExamPaper);

                /* 验证是否可移除 */
                if (validateExamPaperBeOverdue(invalidTime,examTime)) {
                    /*
                      如果失效了，将已发布的考试变为以结束
                     */
                    tbExamPaperDao.updateExamStatus(tbExamPaper.getExamPaperNum());
                }else{
                    if (examStartMap.containsKey(tbExamPaper.getExamPaperNum())){
                        TbExamStart tbExamStart = examStartMap.get(tbExamPaper.getExamPaperNum());
                        if(tbExamStart.getExamEnd()==1){
                            examPaperVo.setExamPaperStatus(ExamPaperStatusEnum.EXAM_IS_START.getStatus());
                        }else{
                            examPaperVo.setExamPaperStatus(ExamPaperStatusEnum.EXAM_UN_START.getStatus());
                        }
                    }
                    result.add(examPaperVo);

                }
            }
        }

        return result;
    }

    @Override
    public List<ExamPaperVo>findExamPaperListByType(Integer examPaperType, int pageNum, int pageSizeNum) {
        int startIndex = (pageNum-1)*pageSizeNum;
        List<TbExamPaper> examPapers = tbExamPaperDao.getExamPaperByType(examPaperType.intValue(),startIndex,pageSizeNum);
        List<ExamPaperVo> result = new ArrayList<>();
        for (TbExamPaper examPaper:examPapers){
            ExamPaperVo examPaperVo = getExamPaperVo(examPaper);
            result.add(examPaperVo);
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
        result.setExamPaperNum(examPaper.getExamPaperNum());
        result.setExamTime(examPaper.getExamTime());
        return result;
    }

    private FindExamPaperPo getExamPaperPo(FindExamPaperVo findExamPaperVo) {
        FindExamPaperPo result = new FindExamPaperPo();
        result.setCreateUnit(findExamPaperVo.getCreateUnit());
        result.setExamPaperName(findExamPaperVo.getExamPaperName());
        result.setInvalidBeginTime(findExamPaperVo.getInvalidBeginTime());
        result.setResponsible(findExamPaperVo.getResponsible());
        result.setInvalidEndTime(findExamPaperVo.getInvalidEndTime());
        result.setExamPaperNum(findExamPaperVo.getExamPaperNum());
        result.setPageSize(findExamPaperVo.getPageSize()==0?10:findExamPaperVo.getPageSize());
        result.setStartIndex((findExamPaperVo.getPage()-1)*result.getPageSize());
        result.setExamPaperType(findExamPaperVo.getExamPaperType());
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
        result.setExamPaperNum(examPaperVo.getExamPaperNum());
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
