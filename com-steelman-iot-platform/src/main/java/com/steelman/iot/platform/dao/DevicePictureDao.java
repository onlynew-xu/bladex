package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DevicePicture;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface DevicePictureDao {

    int deleteByPrimaryKey(Long id);

    int insert(DevicePicture record);

    int insertSelective(DevicePicture record);

    DevicePicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DevicePicture record);

    int updateByPrimaryKey(DevicePicture record);

    //修改
    int updatePictureNameByPrimaryKey(DevicePicture record);

    /**
     * 根据设备Id 获取设备的图片信息
     * @param deviceId
     * @return
     */
    DevicePicture selectByDeviceId(@Param("deviceId") Long deviceId);

    //删除（把表中图片设置为空）
    void deletePictureByPrimaryKey(DevicePicture record);

    List<T> selectByAll(int page, int size);
}