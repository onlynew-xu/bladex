package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_params_smart_super
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceParamSmartSuper implements Serializable {
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备sn
     */
    private String serialNum;

    /**
     * RS485地址 0-247
     */
    private String modbusAddress;

    /**
     * RS485地址波特率 2400，9600，115200
     */
    private String modbusBaudrate;

    /**
     * 数据上报间隔，单位为秒，3-3600
     */
    private String reportTime;

    /**
     * 尖峰时间段上报时间
     */
    private String spikeReportTime;

    /**
     * 高峰时间段上报时间
     */
    private String peakReportTime;

    /**
     * 平峰时间段上报时间
     */
    private String normalReportTime;

    /**
     * 低谷时间段上报时间
     */
    private String valleyReportTime;

    /**
     * 漏电流告警延迟 单位:s
     */
    private String delayAlarmLeaked;

    /**
     * 温度告警延迟 单位:s
     */
    private String delayAlarmTemp;

    /**
     * 电压告警延迟 单位:s
     */
    private String delayAlarmVolt;

    /**
     * 电流告警延迟 单位:s
     */
    private String delayAlarmAmp;

    /**
     * A相CT转换比例 200:1：200
     */
    private String ctRatioA;

    /**
     * B相CT转换比例 200:1：200
     */
    private String ctRatioB;

    /**
     * C相CT转换比例 200:1：200
     */
    private String ctRatioC;

    /**
     * 漏电CT转换比例 200:1：200
     */
    private String ctRatioD;

    /**
     * 高压CT转换比例 200:1：200
     */
    private String ctRatioHighVolt;

    /**
     * 功率因数a 单位：0.001
     */
    private String powerFactorA;

    /**
     * 功率因数b 单位：0.001
     */
    private String powerFactorB;

    /**
     * 功率因数c 单位：0.001
     */
    private String powerFactorC;

    /**
     * 过压a 单位：V
     */
    private String overVoltageA;

    /**
     * 过压b 单位：V
     */
    private String overVoltageB;

    /**
     * 过压c 单位：V
     */
    private String overVoltageC;

    /**
     * 欠压a 单位：V
     */
    private String underVoltageA;

    /**
     * 欠压b 单位：V
     */
    private String underVoltageB;

    /**
     * 欠压c 单位：V
     */
    private String underVoltageC;

    /**
     * 缺相a 单位：V
     */
    private String lackPhaseA;

    /**
     * 缺相b 单位：V
     */
    private String lackPhaseB;

    /**
     * 缺相c 单位：V
     */
    private String lackPhaseC;

    /**
     * 过流a 单位：A
     */
    private String overCurrentA;

    /**
     * 过流b 单位：A 
     */
    private String overCurrentB;

    /**
     * 过流c 单位：A
     */
    private String overCurrentC;

    /**
     * 漏电,单位：mA
     */
    private String overLeaked;

    /**
     * 过温,单位：0.1°C
     */
    private String overTemper;

    /**
     * 电流畸变率a 单位：%
     */
    private String currentThdA;

    /**
     * 电流畸变率b 单位：%
     */
    private String currentThdB;

    /**
     * 电流畸变率c 单位：%
     */
    private String currentThdC;

    /**
     * 电压畸变率a 单位：%
     */
    private String voltageThdA;

    /**
     * 电压畸变率b 单位：%
     */
    private String voltageThdB;

    /**
     * 电压畸变率c 单位：%
     */
    private String voltageThdC;

    /**
     * 电流不平衡度 单位：%
     */
    private String currentUnbalance;

    /**
     * 电压不平衡度 单位：%
     */
    private String voltageUnbalance;

    /**
     * DIDO使能,4 BYTES Byte0：1 DIDO通道4使能 Byte1：1 DIDO通道3使能 Byte2：0 DIDO通道2禁用 Byte3：1 DIDO通道1使能
     */
    private String didoSet;

    /**
     * 系统开关 4Bytes 1110,0：禁止 1：使能; Byte0 定时上报使能开关 ,Byte1峰平谷上报是能开关, Byte2 峰平谷时间使能开关,Byte3高压使能开关
     */
    private String systemSw;

    /**
     * 故障使能开关 5Bytes 11110 0:禁止 1:使能; Byte0 漏电流故障使能开关 Byte1 温度通道1故障使能开关Byte2 温度通道2故障使能开关 Byte3 温度通道3故障使能开关 Byte4 温度通道4故障使能开关
     */
    private String faultEn;

    /**
     * 报警开关: 22 BYTES 0：禁止 1：使能 0 内容过多 详见协议文档
     */
    private String alarmSw;

    /**
     * 尖峰时间段
     */
    private String spike;

    /**
     * 高峰时间段
     */
    private String peak;

    /**
     * 平峰时间段
     */
    private String normal;

    /**
     * 低谷时间段
     */
    private String valley;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}