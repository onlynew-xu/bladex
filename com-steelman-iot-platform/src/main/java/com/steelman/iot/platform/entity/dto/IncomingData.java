package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingData {
    private Long id;
    private String name;
    private Long deviceId;
    private Integer dataFlag;

    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> activePowerMap;

    private Map<String,Object> reactivePowerMap;

    private Map<String,Object> factorDataMap;
}
