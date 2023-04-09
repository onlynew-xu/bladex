package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerFacilityCenter {
    private Long powerId;
    private String powerName;
    private Long powerFacilityId;
    private String powerFacilityName;
    private Integer status;
    private Integer type;
    private String createTime;
}
