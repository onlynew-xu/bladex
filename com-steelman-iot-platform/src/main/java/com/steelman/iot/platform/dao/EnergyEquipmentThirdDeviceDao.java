package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentThirdDevice;

public interface EnergyEquipmentThirdDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentThirdDevice record);

    int insertSelective(EnergyEquipmentThirdDevice record);

    EnergyEquipmentThirdDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentThirdDevice record);

    int updateByPrimaryKey(EnergyEquipmentThirdDevice record);
}