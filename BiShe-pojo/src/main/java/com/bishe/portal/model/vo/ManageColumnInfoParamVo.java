package com.bishe.portal.model.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author gaopan31
 */
@Data
public class ManageColumnInfoParamVo implements Serializable {
    private Integer id;
    private String tittle;
    private String author;
    private String origin;
    private String originUrl;
    private Integer belongColumn;
    private Integer informationStatus;
    private String downTime;
    private Integer isComment;
    private String commentBeginTime;
    private String commentEndTime;
    private String summary;
    private String text;
    private String createUser;
    private MultipartFile columnImg;
    public ManageColumnInfoParamVo(){

    }
}
