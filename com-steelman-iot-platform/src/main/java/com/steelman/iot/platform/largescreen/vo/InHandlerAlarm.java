package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InHandlerAlarm implements Serializable {
    private Long alarmWarnId;

    private String areaName;

    private String buildingName;

    private String deviceTypeName;

    private String alarmItemName;

    private Date createTime;

}
