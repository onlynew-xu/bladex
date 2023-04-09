package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_factory_process_unit
 * @author 
 */
@Data
public class FactoryProcessUnit implements Serializable {
    private Long id;

    /**
     * 采集数据项配置名称
     */
    private String name;

    /**
     * 生产工序
     */
    private String processCode;

    /**
     * 工序单元
     */
    private String processUnitCode;

    /**
     * 重点耗能设备类型
     */
    private String equipmentCode;

    /**
     * 重点耗能设备编号
     */
    private String equipmentUnitCode;

    /**
     * 采集对象类型
     */
    private String energyClassCode;

    /**
     * 能源分类
     */
    private String energyTypeCode;

    /**
     * 用途编码
     */
    private String dataUsageCode;

    /**
     * 数据采集来源
     */
    private String inputType;

    /**
     * 数据最大值
     */
    private String dataValueMax;

    /**
     * 数据最小值
     */
    private String dataValueMin;

    /**
     * 1:全厂，2:生产工序，3:生产工序单元，4:重点耗能设备
     */
    private String scope;

    /**
     * 能耗公司Id
     */
    private Long factoryId;

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