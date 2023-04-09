package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyTypePicture;

public interface EnergyTypePictureService {
    /**
     * 获取图片信息
     * @param energyTypeId
     * @return
     */
    EnergyTypePicture selectByEnergyTypeId(Long energyTypeId);
}
