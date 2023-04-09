package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_compensate_device
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class PowerCompensateDevice implements Serializable {


    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 补偿柜Id
     */
    @ApiModelProperty(value = "补偿柜Id")
    private Long compensateId;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 配电房Id
     */
    @ApiModelProperty(value = "配电房Id")
    private Long powerId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}