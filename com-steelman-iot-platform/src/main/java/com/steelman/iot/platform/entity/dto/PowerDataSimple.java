package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerDataSimple {
    private Long id;
    private String name;
    private Long cabinetCount;
    private String temperature;
    private String humidity;
    private Integer transLoad;
    private Integer defaultFlag;
}
