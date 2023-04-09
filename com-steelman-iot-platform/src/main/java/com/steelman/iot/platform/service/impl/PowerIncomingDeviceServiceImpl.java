package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerIncomingDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 16:19
 * @Description:
 */
@Service("powerIncomingDeviceService")
public class PowerIncomingDeviceServiceImpl extends BaseService implements PowerIncomingDeviceService {
    @Resource
    private PowerIncomingDeviceDao incomingDeviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private MonitorDeviceService monitorDeviceService;

    @Override
    public void insert(PowerIncomingDevice incomingDevice) {
        incomingDeviceDao.insert(incomingDevice);
    }

    @Override
    public void delete(Long id) {
        PowerIncomingDevice incomingDevice = incomingDeviceDao.selectByPrimaryKey(id);
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(incomingDevice.getDeviceId());
        deviceService.update(device);
        incomingDeviceDao.deleteByPrimaryKey(id);


    }


    @Override
    public List<PowerDeviceInfo> getDeviceList(Long incomingId) {
        return incomingDeviceDao.selectByIncomingId(incomingId);
    }

    @Override
    public PowerIncomingDevice getInfo(Long id) {
        return incomingDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(PowerIncomingDevice incomingDevice) {
        incomingDeviceDao.updateByPrimaryKeySelective(incomingDevice);
    }

    @Override
    public List<Map<String, Object>> selectCountMeasurement(Long projectId) {
        List<Map<String, Object>> map = incomingDeviceDao.selectCountMeasurement(projectId);
        return map;
    }

    @Override
    public List<String> selectEnergyTrend(Long projectId, Integer type) {
        Date date = new Date();
        String[] dateArr = DateUtils.getPastDateStrArr(date, 7);
        List<String> list = new ArrayList<>();
        for (String dateStr : dateArr) {
            String energyTrendData = incomingDeviceDao.selectEnergyTrend(projectId, type, dateStr);
            list.add(energyTrendData);
        }
        return list;
    }

    @Override
    public List<String> selectLastEnergyTrend(Long projectId, Integer type) {
        Date date = new Date();
        Date lastDate = DateUtils.getLastMonthDate(date);
        String[] dateArr = DateUtils.getPastDateStrArr(lastDate, 7);
        List<String> list = new ArrayList<>();
        for (String dateStr : dateArr) {
            String energyTrendData = incomingDeviceDao.selectEnergyTrend(projectId, type, dateStr);
            list.add(energyTrendData);
        }

        return list;
    }

    @Override
    public PowerIncomingDevice getInfoByDeviceId(Long deviceId) {
        return incomingDeviceDao.selectByDeviceId(deviceId);
    }


    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;

    @Override
    public Map<String, Object> getPowerIncomingDeviceData(Long incomingId) {
        Map<String, Object> map = new HashMap<>();
        List<PowerIncomingDevice> deviceList = incomingDeviceDao.selectIncomingDevice(incomingId);
        if (!deviceList.isEmpty()) {

            PowerIncomingDevice device = deviceList.get(0);
            map.put("deviceId", device.getDeviceId());
            //实时数据
            DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(device.getDeviceId());
            map.put("realData", dataSmartElec);
            //电流
            List<Map<String, Object>> ampData = deviceDataSmartElecService.selectPowerAmpData(device.getDeviceId());
            map.put("ampData", ampData);
            //电压
            List<Map<String, Object>> voltData = deviceDataSmartElecService.selectPowerVoltData(device.getDeviceId());
            map.put("voltData", voltData);
            //功率因数
            List<Map<String, Object>> factorData = deviceDataSmartElecService.selectPowerFactorData(device.getDeviceId());
            map.put("factorData", factorData);
            //有功功率
            List<Map<String, Object>> activeData = deviceDataSmartElecService.selectPowerActiveData(device.getDeviceId());
            map.put("activeData", activeData);
            //无功功率
            List<Map<String, Object>> reactiveData = deviceDataSmartElecService.selectPowerReactiveData(device.getDeviceId());
            map.put("reactiveData", reactiveData);
            //有功电度
            List<Map<String, Object>> degreeData = deviceMeasurementService.getLastSevenDayTotal(device.getDeviceId());
            map.put("degreeData", degreeData);
            //预警信息
            List<AlarmWarnItemVo> alarmWarnItemVos = alarmWarnService.getDeviceAlarmInfo(device.getProjectId(), device.getDeviceId(), 2000L);
            map.put("alarmData", alarmWarnItemVos);

            //进线柜下所有设备警告比例
            List<Map<String, Object>> alarmCount = alarmWarnService.getDeviceAlarmCount(device.getProjectId(), device.getDeviceId(), 2000L);
            map.put("alarmCount", alarmCount);

            //监控视频
            MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(device.getDeviceId());
            if(!Objects.isNull(monitorDevice)){
                map.put("videoUrl", monitorDevice.getHlsHd());
            }


        }
        return map;
    }

    @Override
    public boolean selectIncomingWhetherAlarm(Long incomingId) {
        return incomingDeviceDao.selectIncomingWhetherAlarm(incomingId);
    }

    @Override
    public boolean selectIncomingWhetherOffLine(Long incomingId) {
        return incomingDeviceDao.selectIncomingWhetherOffLine(incomingId);
    }


    @Override
    public void deleteByDeviceId(Long deviceId) {
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(deviceId);
        deviceService.update(device);
        incomingDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        incomingDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public PowerIncoming findInComingByDeviceId(Long id) {
        return incomingDeviceDao.selectPowerIncomingByDeviceId(id);
    }

    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return incomingDeviceDao.findByDeviceSet(projectId,value);
    }

    @Override
    public Long getBindingStatusDeviceId(Long id) {
        return incomingDeviceDao.getBindingStatus(id);
    }

    @Override
    public Long getBindingOnDevice(Long id) {
        return incomingDeviceDao.getBindingOnDevice(id);
    }

    @Override
    public List<Long> getPowerDeviceList(Long projectId, Long powerId) {
        return incomingDeviceDao.getPowerDeviceList(projectId,powerId);
    }
}
