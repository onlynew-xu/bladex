package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyStatus {
    private Long equipmentId;
    private String equipmentName;
    private Integer status;
    /**
     * lever级别
     */
    private Integer level;
}
