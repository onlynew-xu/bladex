package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_energy_warn_config
 * @author 
 */
@Data
public class EnergyWarnConfig implements Serializable {
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
     * 等级 1:一级 2:二级 3:三级 4:四级 
     */
    private Integer level;

    /**
     * 告警类型 64:日电度 65:月电度 66:总电度
     */
    private Long alarmItemId;

    /**
     * 标准值
     */
    private String stand;

    /**
     * 项目Id
     */
    private Long projectId;

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