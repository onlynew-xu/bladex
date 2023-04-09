package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_compensate_components
 * @author 
 */
@Data
public class PowerCompensateComponents implements Serializable {
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
     * 补偿容量
     */
    private String compensateCap;

    /**
     * 补偿回路
     */
    private String compensateRoad;

    /**
     * 设备状态
     */
    private Integer status;

    /**
     * 补偿柜Id
     */
    private Long compensateId;

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