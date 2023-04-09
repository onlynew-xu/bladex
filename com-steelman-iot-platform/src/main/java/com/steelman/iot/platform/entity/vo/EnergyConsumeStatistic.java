package com.steelman.iot.platform.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统计实体类")
public class EnergyConsumeStatistic {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数值")
    private Integer value;
}
