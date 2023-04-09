package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerFacility {
    private Long powerFacilityId;
    private String powerFacilityName;
    private Long powerId;
    private String powerName;
    private Integer type;
}
