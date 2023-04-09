package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyUploadDao;
import com.steelman.iot.platform.entity.EnergyUpload;
import com.steelman.iot.platform.service.EnergyUploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("energyUploadService")
public class EnergyUploadServiceImpl extends BaseService implements EnergyUploadService {
    @Resource
    private EnergyUploadDao energyUploadDao;

    @Override
    public int save(EnergyUpload energyUpload) {
        return energyUploadDao.insert(energyUpload);
    }
}
