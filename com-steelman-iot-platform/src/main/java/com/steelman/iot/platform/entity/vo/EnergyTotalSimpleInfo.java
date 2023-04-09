package com.steelman.iot.platform.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tang
 * @date 2022/5/30 下午4:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("能耗排行")
public class EnergyTotalSimpleInfo implements Serializable {

    @ApiModelProperty(value = "设备Id")
    private Long equipmentId;

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(value = "能耗等级")
    private Integer level;

    @ApiModelProperty(value = "总能耗")
    private String total;

    @ApiModelProperty(value = "排序字段")
    private Long  orderValue;

}
