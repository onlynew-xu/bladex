package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerFeederLoopDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 15:53
 * @Description:
 */
@Service("powerFeederLoopDeviceService")
public class PowerFeederLoopDeviceServiceImpl extends BaseService implements PowerFeederLoopDeviceService {

    @Resource
    private PowerFeederLoopDeviceDao loopDeviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private PowerFeederLoopService powerFeederLoopService;

    @Override
    public void insert(PowerFeederLoopDevice feederDevice) {
        loopDeviceDao.insert(feederDevice);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceList(Long loopId) {

        return loopDeviceDao.selectByLoopId(loopId);
    }

    @Override
    public void delete(Long id) {
        PowerFeederLoopDevice feederLoopDevice = loopDeviceDao.selectByPrimaryKey(id);
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(feederLoopDevice.getDeviceId());
        deviceService.update(device);
        loopDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerFeederLoopDevice getInfo(Long id) {
        return loopDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(PowerFeederLoopDevice loopDevice) {
        loopDeviceDao.updateByPrimaryKeySelective(loopDevice);
    }

    @Override
    public PowerFeederLoopDevice getInfoByDeviceId(Long deviceId) {
        return loopDeviceDao.selectByDeviceId(deviceId);
    }

    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;

    @Override
    public Map<String, Object> getPowerFeederLoopDeviceData(Long loopId) {
        Map<String, Object> map = new HashMap<>();
        List<PowerFeederLoopDevice> feederLoopDeviceList = loopDeviceDao.selectLoopDevice(loopId);
        PowerFeederLoopDevice feederLoopDevice = feederLoopDeviceList.get(0);
        map.put("deviceId", feederLoopDevice.getDeviceId());
        //实时数据
        DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(feederLoopDevice.getDeviceId());
        map.put("realData", dataSmartElec);
        //电流
        List<Map<String, Object>> ampData = deviceDataSmartElecService.selectPowerAmpData(feederLoopDevice.getDeviceId());
        map.put("ampData", ampData);
        //电压
        List<Map<String, Object>> voltData = deviceDataSmartElecService.selectPowerVoltData(feederLoopDevice.getDeviceId());
        map.put("voltData", voltData);
        //功率因数
        List<Map<String, Object>> factorData = deviceDataSmartElecService.selectPowerFactorData(feederLoopDevice.getDeviceId());
        map.put("factorData", factorData);
        //有功功率
        List<Map<String, Object>> activeData = deviceDataSmartElecService.selectPowerActiveData(feederLoopDevice.getDeviceId());
        map.put("activeData", activeData);
        //无功功率
        List<Map<String, Object>> reactiveData = deviceDataSmartElecService.selectPowerReactiveData(feederLoopDevice.getDeviceId());
        map.put("reactiveData", reactiveData);

        //预警信息
        List<AlarmWarnItemVo> alarmWarnItemVos = alarmWarnService.getDeviceAlarmInfo(feederLoopDevice.getProjectId(),feederLoopDevice.getDeviceId(), 2000L);
        map.put("alarmData", alarmWarnItemVos);

        //进线柜下设备警告比例
        List<Map<String, Object>> alarmCount = alarmWarnService.getDeviceAlarmCount(feederLoopDevice.getProjectId(),feederLoopDevice.getDeviceId(), 2000L);
        map.put("alarmCount", alarmCount);

        //有功电度
        List<Map<String, Object>> degreeData = deviceMeasurementService.getLastSevenDayTotal(feederLoopDevice.getDeviceId());
        map.put("degreeData", degreeData);

        //监控视频
        //监控视频
        MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(feederLoopDevice.getDeviceId());
        if(!Objects.isNull(monitorDevice)){
            map.put("videoUrl", monitorDevice.getHlsHd());
        }
        return map;
    }

    @Resource
    private MonitorDeviceService monitorDeviceService;

    @Override
    public boolean selectFeederWhetherAlarm(Long feederId) {
        return loopDeviceDao.selectFeederWhetherAlarm(feederId);
    }

    @Override
    public boolean selectFeederWhetherOffLine(Long feederId) {
        return loopDeviceDao.selectFeederWhetherOffLine(feederId);
    }



    @Override
    public void removeByDeviceId(Long deviceId) {
        Device device=new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(deviceId);
        deviceService.update(device);
        loopDeviceDao.removeByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        loopDeviceDao.removeByDeviceId(deviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindingDevice(Long feederLoopDeviceId, Long deviceId) {
        //释放设备绑定
        Device device=new Device();
        device.setId(deviceId);
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        loopDeviceDao.deleteByPrimaryKey(feederLoopDeviceId);
        return true;
    }

    @Override
    public Boolean bindingDevice(Long feederLoopId, Long deviceId) {
        Device deviceOld = deviceService.findById(deviceId);
        if(deviceOld.getBindingStatus().equals(1)){
            return false;
        }
        PowerFeederLoop feederLoopInfo = powerFeederLoopService.getFeederLoopInfo(feederLoopId);
        //添加设备绑定回路
        PowerFeederLoopDevice powerFeederLoopDevice=new PowerFeederLoopDevice();
        powerFeederLoopDevice.setDeviceId(deviceId);
        powerFeederLoopDevice.setFeederId(feederLoopInfo.getFeederId());
        powerFeederLoopDevice.setFeederLoopId(feederLoopId);
        powerFeederLoopDevice.setPowerId(feederLoopInfo.getPowerId());
        powerFeederLoopDevice.setProjectId(feederLoopInfo.getProjectId());
        powerFeederLoopDevice.setCreateTime(new Date());
        powerFeederLoopDevice.setUpdateTime(powerFeederLoopDevice.getCreateTime());
        loopDeviceDao.insert(powerFeederLoopDevice);
        //添加设备绑定状态
        Device device=new Device();
        device.setId(deviceId);
        device.setBindingStatus(1);
        device.setBindingType(4);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        return true;
    }

    @Override
    public PowerFeederLoop findFeederLoopByDeviceId(Long id) {
        return loopDeviceDao.selectFeederLoopByDeviceId(id);
    }

    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return loopDeviceDao.findByDeviceSet(projectId,value);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceByFeederId(Long feederId) {
        return loopDeviceDao.getDeviceByFeederId(feederId);
    }

    @Override
    public List<Long> getPowerDeviceList(Long projectId, Long powerId) {
        return loopDeviceDao.getPowerDeviceList(projectId,powerId);
    }
}
