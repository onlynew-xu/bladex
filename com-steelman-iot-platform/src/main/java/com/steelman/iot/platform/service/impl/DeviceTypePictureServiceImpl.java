package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceTypePictureDao;
import com.steelman.iot.platform.entity.DeviceTypePicture;
import com.steelman.iot.platform.service.DeviceTypePictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceTypePictureService")
public class DeviceTypePictureServiceImpl extends BaseService implements DeviceTypePictureService {
    @Resource
    private DeviceTypePictureDao deviceTypePictureDao;
    @Override
    public DeviceTypePicture findByDeviceTypeId(Long deviceTypeId) {
        return deviceTypePictureDao.selectByDeviceTypeId(deviceTypeId);
    }

    @Override
    public List<DeviceTypePicture> listAll() {
        return deviceTypePictureDao.listAll();
    }
}
