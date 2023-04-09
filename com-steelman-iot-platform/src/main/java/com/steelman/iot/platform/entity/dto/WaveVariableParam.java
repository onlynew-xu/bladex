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
public class WaveVariableParam  {
    private Long deviceId;
    private String deviceName;
    private String serialNum;
    private Double jd;
    private Double wd;
    private String transCapacity;
    private String currCT;
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
    private String systemIds;
    private String reportInterval;
    private String transLoad;
    private String powerFactorA;
    private String powerFactorB;
    private String powerFactorC;
    private String overVoltageA;
    private String overVoltageB;
    private String overVoltageC;
    private String underVoltageA;
    private String underVoltageB;
    private String underVoltageC;
    private String lackPhaseA;
    private String lackPhaseB;
    private String lackPhaseC;
    private String overCurrentA;
    private String overCurrentB;
    private String overCurrentC;
    private String currentThdA;
    private String currentThdB;
    private String currentThdC;
    private String voltageThdA;
    private String voltageThdB;
    private String voltageThdC;

    private String manufacturer;
    private String usefulLife;
}
