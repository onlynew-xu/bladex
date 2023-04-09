package com.steelman.iot.platform.energyManager.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_hour_data_smart_super
 * @author 
 */
@Data
public class DeviceHourDataSmartSuper implements Serializable {
    private Long id;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String serialNum;

    /**
     * A相电压相位角,单位：0.01°
     */
    private String voltPhaseAngleA;

    /**
     * B相电压相位角,单位：0.01°
     */
    private String voltPhaseAngleB;

    /**
     * C相电压相位角,单位：0.01°
     */
    private String voltPhaseAngleC;

    /**
     * A相电流相位角,单位：0.01°
     */
    private String ampPhaseAngleA;

    /**
     * B相电流相位角,单位：0.01°
     */
    private String ampPhaseAngleB;

    /**
     * C相电流相位角,单位：0.01°
     */
    private String ampPhaseAngleC;

    /**
     * A相电压 单位0.1V
     */
    private String voltRmsA;

    /**
     * B相电压 单位0.1V
     */
    private String voltRmsB;

    /**
     * C相电压 单位0.1V
     */
    private String voltRmsC;

    /**
     * A相电流 单位：0.01A
     */
    private String ampRmsA;

    /**
     * B相电流 单位：0.01A
     */
    private String ampRmsB;

    /**
     * C相电流 单位：0.01A
     */
    private String ampRmsC;

    /**
     * 漏电电流 单位mA
     */
    private String leaked;

    /**
     * 温度1 单位0.1℃
     */
    private String temp1;

    /**
     * 温度2 单位0.1℃
     */
    private String temp2;

    /**
     * 温度3 单位0.1℃
     */
    private String temp3;

    /**
     * 温度4 单位0.1℃
     */
    private String temp4;

    /**
     * 电流不平衡度 单位 %
     */
    private String currentUnbalance;

    /**
     * 电压不平衡度 单位 %
     */
    private String voltageUnbalance;

    /**
     * A相有功功率 单位：w
     */
    private String activePowerA;

    /**
     * B相有功功率 单位：w
     */
    private String activePowerB;

    /**
     * C相有功功率 单位：w
     */
    private String activePowerC;

    /**
     * A相无功功率 单位：var
     */
    private String reactivePowerA;

    /**
     * B相无功功率 单位：var
     */
    private String reactivePowerB;

    /**
     * C相无功功率 单位：var
     */
    private String reactivePowerC;

    /**
     * A相视在功率 单位：va
     */
    private String apparentPowerA;

    /**
     * B相视在功率 单位：va
     */
    private String apparentPowerB;

    /**
     * C相视在功率 单位：va
     */
    private String apparentPowerC;

    /**
     * A相功率因数 单位：0.001
     */
    private String powerFactorA;

    /**
     * B相功率因数 单位：0.001
     */
    private String powerFactorB;

    /**
     * C相功率因数 单位：0.001
     */
    private String powerFactorC;

    /**
     * 电压线频率 单位：0.01
     */
    private String lineFreq;

    /**
     * A相电流THD 数据 12.1%
     */
    private String thdiA;

    /**
     * B相电流THD 数据 12.1%
     */
    private String thdiB;

    /**
     * C相电流THD 数据 12.1%
     */
    private String thdiC;

    /**
     * A相电压THD 数据 12.1%
     */
    private String thdvA;

    /**
     * B相电压THD 数据 12.1%
     */
    private String thdvB;

    /**
     * C相电压THD 数据 12.1%
     */
    private String thdvC;

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 日
     */
    private String day;

    /**
     * 年月日
     */
    private String yearMonthDay;

    /**
     * 年月日时 格式 :"2021-05-01 12:00:00
     */
    private String dateTimeNow;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}