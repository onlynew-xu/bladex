package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.FactoryUploadConfigureDao;
import com.steelman.iot.platform.entity.FactoryUploadConfigure;
import com.steelman.iot.platform.service.FactoryUploadConfigureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("factoryUploadConfigureService")
public class FactoryUploadConfigureServiceImpl extends BaseService implements FactoryUploadConfigureService {

    @Resource
    private FactoryUploadConfigureDao factoryUploadConfigureDao;

    @Override
    public FactoryUploadConfigure getByFactoryId(Long factoryInfoId) {
        return factoryUploadConfigureDao.getByFactoryId(factoryInfoId);
    }


    @Override
    public int updateFactoryUpload(FactoryUploadConfigure factoryUploadUpdate) {
        return factoryUploadConfigureDao.updateFactoryUpload(factoryUploadUpdate);
    }
}
