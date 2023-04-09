package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/12 0012 17:33
 * @Description:
 */
@Data
public class AlarmWarnVo {
    private Long id;
    private String location;
    private String alarmItemName;
    private String alarmTypeName;
    private Date createTime;


}
