package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_energy_equipment_picture
 * @author 
 */
@Data
public class EnergyEquipmentPicture implements Serializable {
    private Long id;

    /**
     * 能源设备Id
     */
    private Long equipmentId;

    /**
     * 图片url
     */
    private String url;

    /**
     * 文件名
     */
    private String fileName;

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