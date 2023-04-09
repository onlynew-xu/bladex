package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AlarmItemDao;
import com.steelman.iot.platform.entity.AlarmItem;
import com.steelman.iot.platform.service.AlarmItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("alarmItemService")
public class AlarmItemServiceImpl extends BaseService implements AlarmItemService {
    @Resource
    private AlarmItemDao alarmItemDao;

    public void insertSelective(AlarmItem record) {
        alarmItemDao.insertSelective(record);
    }
    public void update(AlarmItem record) {
        alarmItemDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        alarmItemDao.deleteByPrimaryKey(id);
    }
    public AlarmItem findByid(Long id) {
       return alarmItemDao.selectByPrimaryKey(id);
    }

    @Override
    public List<AlarmItem> selectAll() {
        return alarmItemDao.selectAll();
    }
}
