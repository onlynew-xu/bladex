package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/17 0017 14:59
 * @Description:
 */
@Data
public class DeviceCenterVo {
    private Long id;
    private Integer powerTypeId;
    private String name;
    private Date createTime;
    private String url;


}
