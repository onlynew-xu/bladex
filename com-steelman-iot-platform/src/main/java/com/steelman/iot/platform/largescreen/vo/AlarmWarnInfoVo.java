package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmWarnInfoVo implements Serializable {

    private Long alarmWarnId;

    private String areaName;

    private String buildingName;

    private String info;

    private Date createTime;

    private Integer handlerFlag;
}
