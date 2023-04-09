package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_task
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceTask implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 设备状态(上报时)
     */
    @ApiModelProperty(value = "设备状态(上报时)")
    private Integer deviceStatus;

    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String serialNum;

    /**
     * 上报用户Id
     */
    @ApiModelProperty(value = "上报用户Id")
    private Long userId;

    /**
     * 故障详情
     */
    @ApiModelProperty(value = "故障详情")
    private String detail;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 任务状态 0:待执行 1 执行中 2 已完成 3 已过期
     */
    @ApiModelProperty(value = "任务状态 0:待执行 1 执行中 2 已完成 3 已过期")
    private Integer status;

    /**
     * 维保人员Id
     */
    @ApiModelProperty(value = "维保人员Id")
    private Long workerId;

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


    @ApiModelProperty(value = "设备状态")
    private String devStu;

}