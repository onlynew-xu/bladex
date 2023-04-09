package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DevicePicture;
import com.steelman.iot.platform.entity.Pager;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

public interface DevicePictureService {
    /**
     * 获取设备的图片信息
     * @param deviceId
     * @return
     */
    DevicePicture  getByDeviceId(Long deviceId);

    /**
     * 保存设备的图片信息
     * @param devicePicture
     * @return
     */
    int saveDevicePicture(DevicePicture devicePicture);

    Pager<T> selectByAll(Map<String, Integer> params);

    void updateEquipmentPicture(DevicePicture record);

    void deleteEquipmentPicture(DevicePicture record);
}
