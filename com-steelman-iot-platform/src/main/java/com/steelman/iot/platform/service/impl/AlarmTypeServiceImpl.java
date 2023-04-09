package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AlarmTypeDao;
import com.steelman.iot.platform.entity.AlarmType;
import com.steelman.iot.platform.service.AlarmTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author lsj
 * @DATE 2021/4/22 0022 10:03
 * @Description:
 */
@Service("alarmTypeService")
public class AlarmTypeServiceImpl implements AlarmTypeService {

    @Resource
    private AlarmTypeDao alarmTypeDao;

    @Override
    public AlarmType findById(Long alarmTypeId) {
        return alarmTypeDao.selectByPrimaryKey(alarmTypeId);
    }
}
