package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_data_smoke_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceDataSmokeElec implements Serializable {
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
     * 温度
     */
    @ApiModelProperty(value = "温度")
    private String temperature;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 设备位置 详情数据中
     */
    @ApiModelProperty(value = "设备位置")
    private String location;

    /**
     * 消音状态 0 未消音  1 已消音
     */
    @ApiModelProperty(value = "消音状态 0 未消音  1 已消音")
    private String erasure;

}