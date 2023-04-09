package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceTypePicture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceTypePictureDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceTypePicture record);

    int insertSelective(DeviceTypePicture record);

    DeviceTypePicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceTypePicture record);

    int updateByPrimaryKey(DeviceTypePicture record);

    /**
     * 根据设备类型获取图片信息
     * @param deviceTypeId
     * @return
     */
    DeviceTypePicture selectByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId);

    /**
     * 获取所有设备类型的图片信息
     * @return
     */
    List<DeviceTypePicture> listAll();
}