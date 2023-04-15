package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentSecondDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentThirdDevice;

import java.util.List;

public interface EnergyEquipmentThirdDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentThirdDevice record);

    int insertSelective(EnergyEquipmentThirdDevice record);

    EnergyEquipmentThirdDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentThirdDevice record);

    int updateByPrimaryKey(EnergyEquipmentThirdDevice record);

    /**
     * 获取三级设备总数
     */
    int getEnergyEquipmentThirdCount();

    /**
     * 查询三级设备表
     */
    List<EnergyEquipmentThirdDevice> findAll();
}