package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentPower;

public interface EnergyEquipmentPowerDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentPower record);

    int insertSelective(EnergyEquipmentPower record);

    EnergyEquipmentPower selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentPower record);

    int updateByPrimaryKey(EnergyEquipmentPower record);

    void deleteByEquipmentId(Long equipmentId);

    EnergyEquipmentPower selectByEquipmentId(Long equipmentId);
}