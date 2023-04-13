package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentFourthDevice;

public interface EnergyEquipmentFourthDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentFourthDevice record);

    int insertSelective(EnergyEquipmentFourthDevice record);

    EnergyEquipmentFourthDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentFourthDevice record);

    int updateByPrimaryKey(EnergyEquipmentFourthDevice record);

    /**
     * 获取四级设备总数
     */
    int getEnergyEquipmentFourCount();
}