package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("数量统计")
public class SafeDeviceCount {

    @ApiModelProperty(value = "在线数量")
    private Integer inline;

    @ApiModelProperty(value = "离线数量")
    private Integer outline;

    @ApiModelProperty(value = "总数")
    private Integer total;
}
