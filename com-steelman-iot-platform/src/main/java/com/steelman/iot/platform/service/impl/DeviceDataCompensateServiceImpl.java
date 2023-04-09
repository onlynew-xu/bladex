package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataCompensateElecDao;
import com.steelman.iot.platform.entity.DeviceDataCompensateElec;
import com.steelman.iot.platform.service.DeviceDataCompensateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceDataCompensateService")
public class DeviceDataCompensateServiceImpl extends BaseService implements DeviceDataCompensateService {
    @Resource
    private DeviceDataCompensateElecDao deviceDataCompensateElecDao;

    @Override
    public List<DeviceDataCompensateElec>  getLastedTenData(Long deviceId){
        return deviceDataCompensateElecDao.getLastedTenData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataCompensateElecDao.deleteByDeviceId(deviceId);
    }
}
