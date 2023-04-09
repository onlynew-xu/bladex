package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmSimpleInfo implements Serializable {
    private Long deviceId;
    private Long alarmWarnId;
    private Date alarmTime;
    private Long alarmItemId;
    private String alarmItemName;
    private Integer handleFlag;
    private Integer status;
}
