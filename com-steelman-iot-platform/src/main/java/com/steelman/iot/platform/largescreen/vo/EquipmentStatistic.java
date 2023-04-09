package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("设备在线统计")
public class EquipmentStatistic {
    @ApiModelProperty(value = "设备总数")
    private Integer total;
    @ApiModelProperty(value = "在线设备")
    private Integer inline;
    @ApiModelProperty(value = "离线设备")
    private Integer outline;
    @ApiModelProperty(value = "设备在线率")
    private String percent;

}
