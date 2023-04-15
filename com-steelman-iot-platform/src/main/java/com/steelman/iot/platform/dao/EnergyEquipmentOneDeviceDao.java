package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentOneDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public interface EnergyEquipmentOneDeviceDao {

    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentOneDevice record);

    int insertSelective(EnergyEquipmentOneDevice record);

    EnergyEquipmentOneDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentOneDevice record);

    int updateByPrimaryKey(EnergyEquipmentOneDevice record);

    /**
     * 获取一级设备总数
     */

    int getEnergyEquipmentOneCount();

    /**
     * 查询一级设备表
     */
    List<EnergyEquipmentOneDevice> findAll();






}
