package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.PowerCompensateComponents;
import com.steelman.iot.platform.entity.PowerComponents;
import com.steelman.iot.platform.entity.PowerWaveComponents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerComponentsDto implements Serializable {

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
     * 元器件类型名称信息
     */
    private String typeName;

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
     * 安装公司名称信息
     */
    private String installationCompanyName;

    /**
     * 使用公司
     */
    private Long useCompanyId;
    /**
     * 使用公司名称信息
     */
    private String userCompanyName;

    /**
     * 维保公司
     */
    private Long maintenanceCompanyId;
    /**
     * 维保公司名称信息
     */
    private String maintenanceCompanyName;

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
     * 补偿容量
     */
    private String compensateCap;
    /**
     * 补偿回路
     */
    private String  compensateRoad;

    /**
     * 开关状态
     */
    private Integer status;

    /**
     * 补偿柜Id
     */
    private Long compensateId;
    /**
     * 滤波柜Id
     */
    private Long waveId;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public PowerComponentsDto(PowerComponents powerComponents){
        this.id=powerComponents.getId();
        this.brand=powerComponents.getBrand();
        this.effectiveDate=powerComponents.getEffectiveDate();
        this.createTime=powerComponents.getCreateTime();
        this.installationCompanyId=powerComponents.getInstallationCompanyId();
        this.maintenanceCompanyId=powerComponents.getMaintenanceCompanyId();
        this.useCompanyId=powerComponents.getUseCompanyId();
        this.manufacturer=powerComponents.getManufacturer();
        this.name=powerComponents.getName();
        this.powerDeviceId=powerComponents.getPowerDeviceId();
        this.powerDeviceType=powerComponents.getPowerDeviceType();
        this.powerId=powerComponents.getPowerId();
        this.productDate=powerComponents.getProductDate();
        this.projectId=powerComponents.getProjectId();
        this.type=powerComponents.getType();
        Integer type = powerComponents.getType();
        if(type.equals(1)){
            this.typeName="开关类型";
        }else if(type.equals(2)){
            this.typeName="避雷类型";
        }
        this.updateTime=powerComponents.getUpdateTime();
        this.usefulLife=powerComponents.getUsefulLife();
    }
    public PowerComponentsDto(PowerCompensateComponents powerComponents){
        this.id=powerComponents.getId();
        this.brand=powerComponents.getBrand();
        this.effectiveDate=powerComponents.getEffectiveDate();
        this.createTime=powerComponents.getCreateTime();
        this.name=powerComponents.getName();
        this.powerId=powerComponents.getPowerId();
        this.projectId=powerComponents.getProjectId();
        this.type=3;
        this.typeName="补偿元件";
        this.updateTime=powerComponents.getUpdateTime();
        this.compensateCap=powerComponents.getCompensateCap();
        this.compensateRoad=powerComponents.getCompensateRoad();
        this.status=powerComponents.getStatus();
        this.compensateId=powerComponents.getCompensateId();
    }
    public PowerComponentsDto(PowerWaveComponents powerWaveComponents){
        this.id=powerWaveComponents.getId();
        this.brand=powerWaveComponents.getBrand();
        this.effectiveDate=powerWaveComponents.getEffectiveDate();
        this.createTime=powerWaveComponents.getCreateTime();
        this.name=powerWaveComponents.getName();
        this.powerId=powerWaveComponents.getPowerId();
        this.projectId=powerWaveComponents.getProjectId();
        this.type=4;
        this.typeName="滤波元件";
        this.waveId=powerWaveComponents.getWaveId();
        this.updateTime=powerWaveComponents.getUpdateTime();
        this.status=powerWaveComponents.getStatus();
    }

}
