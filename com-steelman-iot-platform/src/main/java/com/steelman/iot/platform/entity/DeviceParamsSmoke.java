package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_params_smoke
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceParamsSmoke implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备主键
     */
    @ApiModelProperty(value = "设备主键")
    private Long deviceId;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String serialNum;

    /**
     * 心跳上报延时时间,单位为秒
     */
    @ApiModelProperty(value = "心跳上报延时时间,单位为秒")
    private String interval;

    /**
     * 告警烟雾浓度告警阈值
     */
    @ApiModelProperty(value = "告警烟雾浓度告警阈值")
    private String smokeConcentration;

    /**
     * 电池电量告警阈值
     */
    @ApiModelProperty(value = "电池电量告警阈值")
    private String electricityQuantity;

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