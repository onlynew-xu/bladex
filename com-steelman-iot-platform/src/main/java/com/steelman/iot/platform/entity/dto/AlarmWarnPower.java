package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmWarnPower {
    private Long id;
    private Long areaId;
    private Long buildingId;
    private Long storeyId;
    private Long roomId;
    private String location;
    private Long alarmItemId;
    private String alarmItemName;
    private Long deviceId;
    private Long systemId;
    private Integer bindingStatus;
    private Integer bindingType;
    private Date createTime;
}
