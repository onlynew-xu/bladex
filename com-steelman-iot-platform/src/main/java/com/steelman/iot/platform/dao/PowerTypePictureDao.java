package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerTypePicture;
import org.apache.ibatis.annotations.Param;

public interface PowerTypePictureDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerTypePicture record);

    int insertSelective(PowerTypePicture record);

    PowerTypePicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerTypePicture record);

    int updateByPrimaryKey(PowerTypePicture record);

    PowerTypePicture getByType(@Param("type") Integer type);
}