package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_energy_warn
 * @author 
 */
@Data
public class EnergyWarn implements Serializable {
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 能源设备Id
     */
    private Long energyId;

    /**
     * 等级 1:一级 2:二级 3:三级 4:四级 5:五级
     */
    private Integer level;

    /**
     * 告警类型
     */
    private Long alarmItemId;

    /**
     * 告警值
     */
    private String value;

    /**
     * 处理标志 0:未处理 1:已处理
     */
    private Integer handleFlag;

    /**
     * 项目Id
     */
    private Long projectId;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}