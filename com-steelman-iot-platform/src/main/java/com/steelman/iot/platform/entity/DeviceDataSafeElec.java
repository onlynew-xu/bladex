package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_data_safe_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceDataSafeElec implements Serializable {
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
     * 温度1 单位0.1℃
     */
    @ApiModelProperty(value = "温度1 单位0.1℃")
    private String temp1;

    /**
     * 温度2 单位0.1℃
     */
    @ApiModelProperty(value = "温度2 单位0.1℃")
    private String temp2;

    /**
     * 温度3 单位0.1℃
     */
    @ApiModelProperty(value = "温度3 单位0.1℃")
    private String temp3;

    /**
     * 温度4 单位0.1℃
     */
    @ApiModelProperty(value = "温度4 单位0.1℃")
    private String temp4;

    /**
     * A相电流 单位0.1A
     */
    @ApiModelProperty(value = "A相电流 单位0.1A")
    private String elctr0;

    /**
     * B相电流 单位0.1A
     */
    @ApiModelProperty(value = "B相电流 单位0.1A")
    private String elctr1;

    /**
     * C相电流 单位0.1A
     */
    @ApiModelProperty(value = "C相电流 单位0.1A")
    private String elctr2;

    /**
     * 漏电流 单位0.1mA
     */
    @ApiModelProperty(value = "漏电流 单位0.1mA")
    private String elctr3;

    /**
     * 第一路三相电A相电压 单位0.1V
     */
    @ApiModelProperty(value = "第一路三相电A相电压 单位0.1V")
    private String volt0;

    /**
     * 第一路三相电B相电压 单位0.1V
     */
    @ApiModelProperty(value = "第一路三相电B相电压 单位0.1V")
    private String volt1;

    /**
     * 第一路三相电C相电压 单位0.1V
     */
    @ApiModelProperty(value = "第一路三相电C相电压 单位0.1V")
    private String volt2;

    /**
     * 第二路三相电压A相电 单位0.1V
     */
    @ApiModelProperty(value = "第二路三相电压A相电 单位0.1V")
    private String volt3;

    /**
     * 第二路三相电B相电压 单位0.1V
     */
    @ApiModelProperty(value = "第二路三相电B相电压 单位0.1V")
    private String volt4;

    /**
     * 第二路三相电C相电压 单位0.1V
     */
    @ApiModelProperty(value = "第二路三相电C相电压 单位0.1V")
    private String volt5;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 设备位置
     */
    @ApiModelProperty(value = "设备位置")
    private String location;

    /**
     * 消音状态 0 未消音  1 已消音
     */
    @ApiModelProperty(value = "消音状态 0 未消音  1 已消音")
    private String erasure;

}