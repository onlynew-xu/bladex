package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.vo.PowerFacilityCenterInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerCenterInfo {

    private Integer powerCount;

    private Integer totalLoad;

    private List<PowerDataSimple> powerDataSimpleList;

    private List<PowerFacilityCenterInfo> powerCenter;



}
