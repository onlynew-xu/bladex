package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyTypePictureDao;
import com.steelman.iot.platform.entity.EnergyTypePicture;
import com.steelman.iot.platform.service.EnergyTypePictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("energyTypePictureService")
public class EnergyTypePictureServiceImpl extends BaseService implements EnergyTypePictureService {
    @Resource
    private EnergyTypePictureDao energyTypePictureDao;

    @Override
    public EnergyTypePicture selectByEnergyTypeId(Long energyTypeId) {
        return energyTypePictureDao.selectByEnergyTypeId(energyTypeId);
    }
}
