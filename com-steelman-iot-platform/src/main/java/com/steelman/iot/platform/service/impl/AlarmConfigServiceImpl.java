package com.steelman.iot.platform.service.impl;
import com.steelman.iot.platform.dao.AlarmConfigDao;
import com.steelman.iot.platform.entity.AlarmConfig;
import com.steelman.iot.platform.service.AlarmConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("alarmConfigService")
public class AlarmConfigServiceImpl extends BaseService implements AlarmConfigService {
    @Resource
    private AlarmConfigDao alarmConfigDao;

    public void insertSelective(AlarmConfig record) {
        alarmConfigDao.insertSelective(record);
    }
    public void update(AlarmConfig record) {
        alarmConfigDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        alarmConfigDao.deleteByPrimaryKey(id);
    }
    public AlarmConfig findByid(Long id) {
       return alarmConfigDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertListRecord(List<AlarmConfig> alarmConfigList) {
        return alarmConfigDao.insertListRecord(alarmConfigList);
    }

    @Override
    public int removeByDeviceSystem( Long deviceId, Long systemId) {
        return alarmConfigDao.removeByDeviceSystem(deviceId,systemId);
    }

    @Override
    public int removeByDeviceId(Long deviceId) {
        return alarmConfigDao.removeByDeviceId(deviceId);
    }
}
