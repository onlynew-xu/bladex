package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerDeviceDao;
import com.steelman.iot.platform.entity.DataTemperaturehumidity;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.PowerDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.DeviceService;
import com.steelman.iot.platform.service.DeviceTypeService;
import com.steelman.iot.platform.service.PowerDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 15:33
 * @Description:
 */
@Service("powerDeviceService")
public class PowerDeviceServiceImpl extends BaseService implements PowerDeviceService {

    @Resource
    private PowerDeviceDao powerDeviceDao;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private DeviceService deviceService;

    @Override
    public void insert(PowerDevice powerDevice) {
        powerDeviceDao.insert(powerDevice);
    }

    @Override
    public List<PowerDevice> selectByPowerId(Long id) {
        List<PowerDevice> deviceList = powerDeviceDao.selectByPowerId(id);
        return deviceList;
    }

    @Override
    public void deleteByPowerId(Long id) {
        powerDeviceDao.deleteByPowerId(id);
    }

    @Override
    public List<PowerDeviceInfo> getPowerDeviceList(Long powerId) {
        List<PowerDeviceInfo> powerDeviceList = powerDeviceDao.selectPowerDevice( powerId);
        return powerDeviceList;

    }

    @Override
    public void delete(Long id) {
        powerDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerDevice getInfo(Long id) {
        return powerDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(PowerDevice powerDevice) {
        powerDeviceDao.updateByPrimaryKeySelective(powerDevice);
    }

    @Override
    public DataTemperaturehumidity selectPowerTemperatureHumidity(Long powerId) {
        return powerDeviceDao.selectTemperatureDevice(powerId);
    }

    @Override
    public PowerDevice getInfoByDeviceId(Long deviceId) {
        return powerDeviceDao.selectByDeviceId(deviceId);
    }



    @Override
    public void deleteByDeviceId(Long deviceId) {
        //重置设备绑定状态
        Device device = new Device();
        device.setId(deviceId);
        device.setBindingType(-1);
        device.setBindingStatus(0);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        powerDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        powerDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public String findPowerNameByDeviceId(Long id) {
        return powerDeviceDao.selectPowerNameByDeviceId(id);
    }



    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return powerDeviceDao.findByDeviceSet(projectId,value);
    }
}
