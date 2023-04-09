package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafeCompensateDeviceCenter implements Serializable {
    private Long deviceId;
    private String deviceName;
    private String serialNum;
    private Long projectId;
    private Long deviceTypeId;
    private String deviceTypeName;
    private String location;
    private Integer status;
    private Integer erasure;
    private String videoUrl;
    private String pictureUrl;
    private Integer dataFlag;
    private Map<String,Object> dianYa=new LinkedHashMap<>();
    private Map<String,Object> dianLiu=new LinkedHashMap<>();
    private Map<String,Object> activePower=new LinkedHashMap<>();
    private Map<String,Object> inActivePower=new LinkedHashMap<>();
    private Map<String,Object> powerFactor =new LinkedHashMap<>();
}
