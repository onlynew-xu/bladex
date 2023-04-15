package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_energy_equipment_second_device
 * @author 
 */
@Data
public class EnergyEquipmentSecondDevice implements Serializable {
    private Long id;

    /**
     * 能源设备Id
     */
    private Long equipmentSecondId;

    /**
     * 设备类型
     */
    private Long deviceTypeId;

    /**
     * 设备Id
     */
    private Long deviceId;

    /**
     * 系统Id
     */
    private Long systemId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 生成时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}