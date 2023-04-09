package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/1 0001 14:51
 * @Description:
 */
@Data
public class InspectionInfo {
    private Long id;
    private String content;
    private String title;
    private String evenWeek;
    private String evenMonth;
    private Long userId;
    private Date beginTime;
    private Date endTime;
    private Integer status;
    private Long projectId;
    private String username;
}
