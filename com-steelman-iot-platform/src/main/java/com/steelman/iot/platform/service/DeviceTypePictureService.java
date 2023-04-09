package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceTypePicture;

import java.util.List;

public interface DeviceTypePictureService {
    /**
     * 根据设备类型获取设备的图片
     * @param deviceTypeId
     * @return
     */
    DeviceTypePicture findByDeviceTypeId(Long deviceTypeId);

    List<DeviceTypePicture> listAll();
}
