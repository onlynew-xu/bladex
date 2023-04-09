package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyTypePicture;
import org.apache.ibatis.annotations.Param;

public interface EnergyTypePictureDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyTypePicture record);

    int insertSelective(EnergyTypePicture record);

    EnergyTypePicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyTypePicture record);

    int updateByPrimaryKey(EnergyTypePicture record);

    /**
     * 获取图片信息
     * @param energyTypeId
     * @return
     */
    EnergyTypePicture selectByEnergyTypeId(@Param("energyTypeId") Long energyTypeId);
}