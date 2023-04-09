package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "电房基本电压电流数据")
public class PowerRecentInfo {

    @ApiModelProperty(value = "电压数据")
    private Map<String,Object> dianYa=new LinkedHashMap<>();

    @ApiModelProperty(value = "电流数据")
    private Map<String,Object> dianLiu=new LinkedHashMap<>();

    @ApiModelProperty(value = "有功功率")
    private Map<String,Object> activePower=new LinkedHashMap<>();

    @ApiModelProperty(value = "无功功率")
    private Map<String,Object> reactivePower=new LinkedHashMap<>();

}
