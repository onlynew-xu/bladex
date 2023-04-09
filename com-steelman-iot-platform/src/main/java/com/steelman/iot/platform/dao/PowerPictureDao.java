package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerPicture;

public interface PowerPictureDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerPicture record);

    int insertSelective(PowerPicture record);

    PowerPicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerPicture record);

    int updateByPrimaryKey(PowerPicture record);

    PowerPicture getByPowerId(Long powerId);
}