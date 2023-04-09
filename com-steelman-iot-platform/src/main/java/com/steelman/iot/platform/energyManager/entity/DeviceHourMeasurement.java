package com.steelman.iot.platform.energyManager.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_hour_measurement
 * @author 
 */
@Data
public class DeviceHourMeasurement implements Serializable {
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * sn码
     */
    private String serialNum;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 当日尖时电度数 单位0.001kwh
     */
    private String daySpike;

    /**
     * 当日高峰期电度数 单位0.001kwh
     */
    private String dayPeak;

    /**
     * 当日平峰期电度数 单位0.001kwh
     */
    private String dayNormal;

    /**
     * 当日低谷期电度数 单位0.001kwh
     */
    private String dayValley;

    /**
     * 当日总电度数 单位0.001kwh
     */
    private String dayTotal;

    /**
     * 当日尖时电费 单位元
     */
    private String daySpikePrice;

    /**
     * 当日高峰电费 单位元
     */
    private String dayPeakPrice;

    /**
     * 当日平峰电费 单位元
     */
    private String dayNormalPrice;

    /**
     * 当日低谷期电费 单位元
     */
    private String dayValleyPrice;

    /**
     * 当日总电费 单位元
     */
    private String dayTotalPrice;

    /**
     * 月尖时高峰电度 单位0.001kwh
     */
    private String monthSpike;

    /**
     * 月高峰期电度 单位0.001kwh
     */
    private String monthPeak;

    /**
     * 月平峰期电度 单位0.001kwh
     */
    private String monthNormal;

    /**
     * 月低谷期电度 单位0.001kwh
     */
    private String monthValley;

    /**
     * 月总电度 单位0.001kwh
     */
    private String monthTotal;

    /**
     * 尖时总电度数 单位0.001kwh
     */
    private String totalSpike;

    /**
     * 高峰期总电度数 单位0.001kwh
     */
    private String totalPeak;

    /**
     * 平峰期总电度数 单位0.001kwh
     */
    private String totalNormal;

    /**
     * 低谷期总电度数 单位0.001kwh
     */
    private String totalValley;

    /**
     * 总电度数 单位0.001kwh
     */
    private String totalTotal;

    /**
     * 当日尖时无功电度数单位0.001kvar
     */
    private String reactiveDaySpike;

    /**
     * 当日高峰期无功电度数 单位0.001Kvar
     */
    private String reactiveDayPeak;

    /**
     * 当日平峰期无功电度数 单位0.001Kvar
     */
    private String reactiveDayNormal;

    /**
     * 当日低谷期无功电度数 单位0.001Kvar
     */
    private String reactiveDayValley;

    /**
     * 当日总无功电度数 单位0.001kwh
     */
    private String reactiveDayTotal;

    /**
     * 月尖时无功电度 单位0.001kwh
     */
    private String reactiveMonthSpike;

    /**
     * 月高峰期无功电度 单位0.001Kvar
     */
    private String reactiveMonthPeak;

    /**
     * 月平峰期无功电度 单位0.001Kvar
     */
    private String reactiveMonthNormal;

    /**
     * 月低谷期无功电度 单位0.001Kvar
     */
    private String reactiveMonthValley;

    /**
     * 月总无功电度 单位0.001Kvar
     */
    private String reactiveMonthTotal;

    /**
     * 尖时总无功电度 单位 0.001kvar
     */
    private String reactiveTotalSpike;

    /**
     * 高峰期总无功电度数 单位0.001Kvar
     */
    private String reactiveTotalPeak;

    /**
     * 平峰期总无功电度数 单位0.001Kvar
     */
    private String reactiveTotalNormal;

    /**
     * 低谷期总无功电度数 单位0.001Kvar
     */
    private String reactiveTotalValley;

    /**
     * 总无功电度数 单位0.001Kvar
     */
    private String reactiveTotalTotal;

    /**
     * 年
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 日
     */
    private String day;

    /**
     * 年月日 格式 :"2021-05-01"
     */
    private String yearMonthDay;

    /**
     * 年月日时 格式 :"2021-05-01 12:00:00
     */
    private String dateTimeBegin;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}