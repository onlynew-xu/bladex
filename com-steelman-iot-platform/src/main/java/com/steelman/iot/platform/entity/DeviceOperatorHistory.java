package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_operator_history
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceOperatorHistory implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * sn码
     */
    @ApiModelProperty(value = "sn码")
    private String serialNum;

    /**
     * 用户操作
     */
    @ApiModelProperty(value = "用户操作")
    private String operation;

    /**
     * 返回状态码
     */
    @ApiModelProperty(value = "返回状态码")
    private Integer returnCode;

    /**
     * 操作状态0 失败 1成功
     */
    @ApiModelProperty(value = "操作状态0 失败 1成功")
    private Integer status;

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