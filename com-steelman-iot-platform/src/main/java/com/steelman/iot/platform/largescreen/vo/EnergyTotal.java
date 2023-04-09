package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("消耗统计")
public class EnergyTotal {

    @ApiModelProperty(value = "电能耗")
    private String electric;

    @ApiModelProperty(value = "水能耗")
    private String water;

    @ApiModelProperty(value = "气能耗")
    private String gas;
}
