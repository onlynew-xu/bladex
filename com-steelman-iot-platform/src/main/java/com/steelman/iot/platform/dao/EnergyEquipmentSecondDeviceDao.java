package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentOneDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentSecondDevice;

import java.util.List;

public interface EnergyEquipmentSecondDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentSecondDevice record);

    int insertSelective(EnergyEquipmentSecondDevice record);

    EnergyEquipmentSecondDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentSecondDevice record);

    int updateByPrimaryKey(EnergyEquipmentSecondDevice record);

    /**
     * 获取二级设备总数
     */
    int getEnergyEquipmentSecondCount();

    /**
     * 查询二级设备表
     */
    List<EnergyEquipmentSecondDevice> findAll();
}