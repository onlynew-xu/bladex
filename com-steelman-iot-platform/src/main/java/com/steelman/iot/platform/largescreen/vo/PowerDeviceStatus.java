package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "电房绑定的智能设备状态统计")
public class PowerDeviceStatus {

    @ApiModelProperty(value = "设备总数")
    private Integer total;

    @ApiModelProperty(value = "设备数量")
    private Integer inline;

    @ApiModelProperty(value = "离线数量")
    private Integer outLine;

    @ApiModelProperty(value = "设备在线率")
    private String percent;
}
