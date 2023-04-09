package com.steelman.iot.platform.energyManager.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_hour_differ_measurement
 * @author 
 */
@Data
public class DeviceHourDifferMeasurement implements Serializable {
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
     * 时尖时电度数 单位0.001kwh
     */
    private String hourSpike;

    /**
     * 时高峰期电度数 单位0.001kwh
     */
    private String hourPeak;

    /**
     * 时平峰期电度数 单位0.001kwh
     */
    private String hourNormal;

    /**
     * 时低谷期电度数 单位0.001kwh
     */
    private String hourValley;

    /**
     * 时总电度数 单位0.001kwh
     */
    private String hourTotal;

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
     * 年月日时 格式 :"2021-05-01 13:00:00
     */
    private String dateTimeEnd;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}