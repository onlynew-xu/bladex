package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_energy_equipment_power
 * @author 
 */
@Data
public class EnergyEquipmentPower implements Serializable {
    private Long id;

    /**
     * 能源设备Id
     */
    private Long equipmentId;

    /**
     * 电房Id
     */
    private Long powerId;

    /**
     * 变压器Id
     */
    private Long transformerId;

    /**
     * 配电箱Id
     */
    private Long boxId;

    /**
     * 回路Id
     */
    private Long boxLoopId;

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