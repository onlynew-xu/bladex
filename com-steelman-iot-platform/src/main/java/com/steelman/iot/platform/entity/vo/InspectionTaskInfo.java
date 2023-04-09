package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/2 0002 13:51
 * @Description:
 */
@Data
public class InspectionTaskInfo {
    private Long id;
    private String title;
    private String content;
    private String username;
    private Integer status;
    private Date doingTime;
    private Date createTime;
}
