package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_components
 * @author 
 */
@Data
public class PowerComponents implements Serializable {
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
     * 元器件类型 1 开关类型 2 避雷类型
     */
    private Integer type;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产日期
     */
    private String productDate;

    /**
     * 使用寿命
     */
    private String usefulLife;

    /**
     * 质保期
     */
    private String effectiveDate;

    /**
     * 安装公司
     */
    private Long installationCompanyId;

    /**
     * 使用公司
     */
    private Long useCompanyId;

    /**
     * 维保公司
     */
    private Long maintenanceCompanyId;

    /**
     * 电房设备类型 0 发电机 1 进线柜 2 补偿柜 3 滤波柜 4 馈线柜 5 配电箱
     */
    private Integer powerDeviceType;

    /**
     * 电房设备Id
     */
    private Long powerDeviceId;

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