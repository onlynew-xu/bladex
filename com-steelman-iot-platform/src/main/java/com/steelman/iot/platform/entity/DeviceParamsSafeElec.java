package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_params_safe_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceParamsSafeElec implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String serialNum;

    /**
     * 上报延时时间,单位为秒，高位在前 3-3600
     */
    @ApiModelProperty(value = "上报延时时间,单位为秒，高位在前 3-3600")
    private String upInterval;

    /**
     * 电压告警延时时间,单位为秒，高位在前 1-360
     */
    @ApiModelProperty(value = "电压告警延时时间,单位为秒，高位在前 1-360")
    private String voltInterval;

    /**
     * 电流告警延时时间,单位为秒，高位在前 1-360
     */
    @ApiModelProperty(value = "电流告警延时时间,单位为秒，高位在前 1-360")
    private String currInterval;

    /**
     * 漏电电流告警延时时间,单位为秒，高位在前 1-360
     */
    @ApiModelProperty(value = "漏电电流告警延时时间,单位为秒，高位在前 1-360")
    private String leakInterval;

    /**
     * 温度告警延时时间,单位为秒，高位在前 1-360
     */
    @ApiModelProperty(value = "温度告警延时时间,单位为秒，高位在前 1-360")
    private String tempInterval;

    /**
     * 电压过压告警门限，单位 0.1 伏
     */
    @ApiModelProperty(value = "电压过压告警门限，单位 0.1 伏")
    private String voltHigh;

    /**
     * 电压欠压告警门限，单位 0.1 伏
     */
    @ApiModelProperty(value = "电压欠压告警门限，单位 0.1 伏")
    private String voltLow;

    /**
     * 漏电电流告警门限，单位 0.1 毫安
     */
    @ApiModelProperty(value = "漏电电流告警门限，单位 0.1 毫安")
    private String currLeak;

    /**
     * 电流过流告警门限，单位 0.1 A
     */
    @ApiModelProperty(value = "电流过流告警门限，单位 0.1 A")
    private String currHigh;

    /**
     * 温度过温告警门限，单位 0.1 摄氏度
     */
    @ApiModelProperty(value = "温度过温告警门限，单位 0.1 摄氏度")
    private String tempHigh;

    /**
     * 告警使能标志 详见设备协议
     */
    @ApiModelProperty(value = "告警使能标志 详见设备协议")
    private String alarmEn;

    /**
     * 100：成功 -1：正在进行数据解析,请等待。 -2：Json 格式错误 -3：设置失败 -100: 其他
     */
    @ApiModelProperty(value = "100：成功 -1：正在进行数据解析,请等待。 -2：Json 格式错误 -3：设置失败 -100: 其他")
    private String returnCode;

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