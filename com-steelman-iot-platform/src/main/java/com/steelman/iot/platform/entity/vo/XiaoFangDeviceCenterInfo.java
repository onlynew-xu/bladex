package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoFangDeviceCenterInfo implements Serializable {
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
    private Map<String,Object> dianYaOne=new LinkedHashMap<>();
    private Map<String,Object> dianYaTwo=new LinkedHashMap<>();
    private Map<String,Object> dianLiu=new LinkedHashMap<>();
}
