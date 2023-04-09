package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerPictureDao;
import com.steelman.iot.platform.entity.PowerPicture;
import com.steelman.iot.platform.service.PowerPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tang
 * @date 2022/5/27 下午5:40
 */
@Service("powerPictureService")
public class PowerPictureServiceImpl extends BaseService implements PowerPictureService {
    @Resource
    private PowerPictureDao powerPictureDao;


    @Override
    public PowerPicture getByPowerId(Long powerId) {
        return powerPictureDao.getByPowerId(powerId);
    }
}
