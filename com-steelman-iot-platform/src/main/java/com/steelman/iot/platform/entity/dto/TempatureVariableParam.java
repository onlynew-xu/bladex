package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.DeviceSystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempatureVariableParam {
    private Long deviceId;
    private String deviceName;
    private String serialNum;
    private Double jd;
    private Double wd;
    private Integer status;
    private String videoUrl;
    private Long monitorId;
    private String monitorName;
    private String pictureUrl;
    private Long areaId;
    private String areaName;
    private Long buildingId;
    private String buildingName;
    private Long storeyId;
    private String storeyName;
    private Long roomId;
    private String roomName;
    private Integer locationFlag;
    private String location;
    private List<DeviceSystem> systemList=new ArrayList<>();
    private String systemId;
    private String manufacturer;
    private String usefulLife;
    private String interval;
}
