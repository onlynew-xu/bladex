package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRankReport {

    private Long equipmentId;

    private String equipmentName;

    private  String total;

    private String spike;

    private String peak;

    private String normal;

    private String valley;

    private String percent;


    private Long totalCount;
}
