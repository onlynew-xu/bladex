package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "电房温湿度数据")
public class PowerDataInfo {

    @ApiModelProperty(value = "电房Id")
    private Long powerId;

    @ApiModelProperty(value = "电房名称")
    private String powerName;

    @ApiModelProperty(value = "电房温度")
    private String temperature;

    @ApiModelProperty(value = "电房湿度")
    private String humidity;
}
