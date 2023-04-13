package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *iot_energy_equipment_one
 */
@Data
public class EnergyEquipmentOne implements Serializable {
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
     * 额定功率
     */
    private String ratedPower;

    /**
     * 生产日期
     */
    private Date productionDate;

    /**
     * 使用公司
     */
    private Long companyId;

    /**
     * 区域Id
     */
    private Long areaId;

    /**
     * 楼Id
     */
    private Long buildingId;

    /**
     * 楼层Id
     */
    private Long storeyId;

    /**
     * 房间号Id
     */
    private Long roomId;

    /**
     * 自定义设备位置
     */
    private String location;

    /**
     * 能源设备类型
     */
    private Long energyTypeId;

    /**
     * 能耗设备类型
     */
    private Long consumeTypeId;


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
