package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyMeasureDateTime  implements Serializable {
    private Long equipmentId;
    private String equipmentName;
    private String consumeTypeName;
    private String  energyTypeName;
    private String  spikeMeasure;
    private String  peakMeasure;
    private String  normalMeasure;
    private String  valleyMeasure;
    private String totalMeasure;
    private Float totalOrder;

}
