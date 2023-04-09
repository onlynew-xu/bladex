package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tang
 * date 2020-12-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmWarnDto implements Serializable {

    private Long id;
    private Long deviceId;
    private String deviceName;
    private Long deviceTypeId;
    private String deviceTypeName;
    private String serialNum;
    private Long areaId;
    private String areaName;
    private Long buildingId;
    private String buildingName;
    private Long storeyId;
    private String storeyName;
    private Long roomId;
    private String roomName;
    private String location;
    private Long systemId;
    private Long projectId;
    private Long alarmItemId;
    private String alarmItemName;
    private String alarmItemParentName;
    private Long alarmTypeId;
    private String alarmValue;
    private Integer handleFlag;
    private Integer createTask;
    private Integer taskType;
    private Long taskId;
    private String year;
    private String month;
    private String day;
    private Date createTime;
    private Date updateTime;


}
