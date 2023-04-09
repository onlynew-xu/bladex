package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_power_device
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class PowerDevice implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 配电房Id
     */
    @ApiModelProperty(value = "配电房Id")
    private Long powerId;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 生成时间
     */
    @ApiModelProperty(value = "生成时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}