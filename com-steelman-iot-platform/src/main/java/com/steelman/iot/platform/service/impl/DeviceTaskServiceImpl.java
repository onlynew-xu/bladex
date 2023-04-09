package com.steelman.iot.platform.service.impl;
import com.steelman.iot.platform.dao.DeviceTaskDao;
import com.steelman.iot.platform.entity.DeviceTask;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.service.DeviceTaskService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("deviceTaskService")
public class DeviceTaskServiceImpl extends BaseService implements DeviceTaskService {
    @Resource
    private DeviceTaskDao deviceTaskDao;

    public void insertSelective(DeviceTask record) {
        deviceTaskDao.insertSelective(record);
    }

    public void insert(DeviceTask record) {
        deviceTaskDao.insert(record);
    }

    public void update(DeviceTask record) {
        deviceTaskDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceTaskDao.deleteByPrimaryKey(id);
    }
    public DeviceTask findById(Long id) {
       return deviceTaskDao.selectByPrimaryKey(id);
    }
    public Pager<T> findByDeviceId(Map<String, Integer> params, Long deviceId) {
        List<T> list =deviceTaskDao.findByDeviceId((params.get("page")-1)*params.get("size"),params.get("size"),deviceId);
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public int removeByDeviceSystem(Long projectId, Long deviceId, Long systemId) {
        return deviceTaskDao.removeByDeviceSystem(projectId,deviceId,systemId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceTaskDao.deleteByDeviceId(deviceId);
    }
}
