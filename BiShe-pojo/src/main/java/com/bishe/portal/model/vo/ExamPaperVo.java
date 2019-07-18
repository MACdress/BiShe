package com.bishe.portal.model.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author 73515
 */
@Data
public class ExamPaperVo implements Serializable {
    private String examPaperName;
    private Integer examPaperType;
    private String invalidTime;
    private String createUnit;
    private String responsible;
    private Integer status;
    private Integer examTime;
    private String examPaperNum;
    private Integer examPaperStatus;
    private MultipartFile examPaperImg;
}
