package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentPicture;

public interface EnergyEquipmentPictureDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentPicture record);

    int insertSelective(EnergyEquipmentPicture record);

    EnergyEquipmentPicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentPicture record);

    int updateByPrimaryKey(EnergyEquipmentPicture record);
}