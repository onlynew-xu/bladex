package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("电房中设备的数量统计类")
public class PowerDeviceCount {

    @ApiModelProperty(value = "电房设备数量总计")
    private Integer total;

    @ApiModelProperty(value ="每个电房中对应的设备总数的统计 map中数据示例(powerName:1号电房,count:设备数量)")
    private List<Map<String,Object>> powerDevice;
}
