package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerCompensateDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 14:44
 * @Description:
 */
@Service("powerCompensateDeviceService")
public class PowerCompensateDeviceServiceImpl extends BaseService implements PowerCompensateDeviceService {
    @Resource
    private PowerCompensateDeviceDao compensateDeviceDao;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private MonitorDeviceService monitorDeviceService;

    @Override
    public void insert(PowerCompensateDevice compensateDevice) {
        compensateDeviceDao.insert(compensateDevice);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceList(Long compensateId) {
        return compensateDeviceDao.selectByCompensateId(compensateId);
    }

    @Override
    public void delete(Long id) {
        PowerCompensateDevice compensateDevice = compensateDeviceDao.selectByPrimaryKey(id);
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(compensateDevice.getDeviceId());
        deviceService.update(device);
        compensateDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerCompensateDevice getInfoByDeviceId(Long deviceId) {
        return compensateDeviceDao.selectByDeviceId(deviceId);
    }

    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private AlarmWarnService alarmWarnService;

    @Override
    public Map<String, Object> getPowerCompensateDeviceData(Long compensateId) {
        Map<String, Object> map = new HashMap<>();
        List<PowerCompensateDevice> deviceList = compensateDeviceDao.selectCompensateDevice(compensateId);
        if (!deviceList.isEmpty()) {
            PowerCompensateDevice device = deviceList.get(0);
            map.put("deviceId", device.getDeviceId());
            //平均功率因数
            DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(device.getDeviceId());
            BigDecimal total = new BigDecimal("0");
            total = total.add(new BigDecimal(dataSmartElec.getPowerFactorA())).add(new BigDecimal(dataSmartElec.getPowerFactorB())).add(new BigDecimal(dataSmartElec.getPowerFactorC())).divide(new BigDecimal("3")).setScale(3, BigDecimal.ROUND_HALF_UP);
            map.put("avgPowerFactor", total);
            //实时数据
            map.put("realData", dataSmartElec);
            //电流
            List<Map<String, Object>> ampData = deviceDataSmartElecService.selectPowerAmpData(device.getDeviceId());
            map.put("ampData", ampData);
            //功率因数
            List<Map<String, Object>> factorData = deviceDataSmartElecService.selectPowerFactorData(device.getDeviceId());
            map.put("factorData", factorData);
            //电压
            List<Map<String, Object>> voltData = deviceDataSmartElecService.selectPowerVoltData(device.getDeviceId());
            map.put("voltData", voltData);
            //无功功率
            List<Map<String, Object>> reactiveData = deviceDataSmartElecService.selectPowerReactiveData(device.getDeviceId());
            map.put("reactiveData", reactiveData);
            //有功功率
            List<Map<String, Object>> activeData = deviceDataSmartElecService.selectPowerActiveData(device.getDeviceId());
            map.put("activeData", activeData);

            //有功电度
            List<Map<String, Object>> degreeData = deviceMeasurementService.getLastSevenDayTotal(device.getDeviceId());
            map.put("degreeData", degreeData);

            //预警信息
            List<AlarmWarnItemVo> alarmWarnItemVos = alarmWarnService.getDeviceAlarmInfo(device.getProjectId(),device.getDeviceId(), 2000L);
            map.put("alarmData", alarmWarnItemVos);

            //进线柜下所有设备警告比例
            List<Map<String, Object>> alarmCount = alarmWarnService.getDeviceAlarmCount(device.getProjectId(),device.getDeviceId(), 2000L);
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
    public boolean selectCompensateWhetherAlarm(Long compensateId) {
        return compensateDeviceDao.selectCompensateWhetherAlarm(compensateId);
    }

    @Override
    public boolean selectCompensateWhetherOffLine(Long compensateId) {
        return compensateDeviceDao.selectCompensateWhetherOffLine(compensateId);
    }

    @Override
    public void deleteByDeviceId(Long deviceId) {
        Device device=new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(deviceId);
        deviceService.update(device);
        compensateDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        compensateDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public PowerCompensate findCompensateByDeviceId(Long id) {

        return compensateDeviceDao.selectCompensateByDeviceId(id);
    }

    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return compensateDeviceDao.findByDeviceSet(projectId,value);
    }

    @Override
    public List<Long> getPowerDeviceList(Long projectId, Long powerId) {
        return compensateDeviceDao.getPowerDeviceList(projectId,powerId);
    }
}
