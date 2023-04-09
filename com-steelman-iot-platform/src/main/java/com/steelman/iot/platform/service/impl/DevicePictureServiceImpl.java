package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DevicePictureDao;
import com.steelman.iot.platform.entity.DevicePicture;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.service.DevicePictureService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("devicePictureService")
public class DevicePictureServiceImpl extends  BaseService implements DevicePictureService {
    @Resource
    private DevicePictureDao devicePictureDao;
    @Override
    public DevicePicture getByDeviceId(Long deviceId) {
        return devicePictureDao.selectByDeviceId(deviceId);
    }

    @Override
    public int saveDevicePicture(DevicePicture devicePicture) {
        return devicePictureDao.insertSelective(devicePicture);
    }

    @Override
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list = devicePictureDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public void updateEquipmentPicture(DevicePicture record) {
        devicePictureDao.updatePictureNameByPrimaryKey(record);
    }

    @Override
    public void deleteEquipmentPicture(DevicePicture record) {
        devicePictureDao.deletePictureByPrimaryKey(record);
    }
}
