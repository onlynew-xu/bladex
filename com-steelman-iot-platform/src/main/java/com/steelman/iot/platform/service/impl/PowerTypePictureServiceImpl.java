package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerTypePictureDao;
import com.steelman.iot.platform.entity.PowerTypePicture;
import com.steelman.iot.platform.service.PowerTypePictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("powerTypePictureService")
public class PowerTypePictureServiceImpl extends BaseService implements PowerTypePictureService {
    @Resource
    private PowerTypePictureDao powerTypePictureDao;

    @Override
    public PowerTypePicture getByType(Integer type) {
        return powerTypePictureDao.getByType(type);
    }
}
