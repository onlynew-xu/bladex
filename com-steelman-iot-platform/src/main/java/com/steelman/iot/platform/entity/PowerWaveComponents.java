package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_wave_components
 * @author
 */
@Data
public class PowerWaveComponents implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 质保期
     */
    private String effectiveDate;

    /**
     * 设备状态
     */
    private Integer status;

    /**
     * 补偿柜Id
     */
    private Long waveId;

    /**
     * 配电房Id
     */
    private Long powerId;

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