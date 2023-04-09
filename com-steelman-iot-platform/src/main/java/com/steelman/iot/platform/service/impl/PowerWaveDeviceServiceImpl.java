package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerWaveDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 10:49
 * @Description:
 */
@Service("powerWaveDeviceService")
public class PowerWaveDeviceServiceImpl extends BaseService implements PowerWaveDeviceService {
    @Resource
    private PowerWaveDeviceDao waveDeviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceTypeService deviceTypeService;

    @Override
    public void insert(PowerWaveDevice waveDevice) {
        waveDeviceDao.insert(waveDevice);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceList(Long waveId) {
        return waveDeviceDao.selectByWaveId(waveId);
    }

    @Override
    public void delete(Long id) {
        PowerWaveDevice incomingDevice = waveDeviceDao.selectByPrimaryKey(id);
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(incomingDevice.getDeviceId());
        deviceService.update(device);
        waveDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerWaveDevice getInfo(Long id) {
        return waveDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(PowerWaveDevice waveDevice) {
        waveDeviceDao.updateByPrimaryKeySelective(waveDevice);
    }

    @Override
    public PowerWaveDevice getInfoByDeviceId(Long deviceId) {

        return waveDeviceDao.selectByDeviceId(deviceId);
    }

    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private AlarmWarnService alarmWarnService;

    @Override
    public Map<String, Object> getPowerWaveDeviceData(Long waveId) {
        Map<String, Object> map = new HashMap<>();
        List<PowerWaveDevice> deviceList = waveDeviceDao.selectWaveDevice(waveId);
        if (!deviceList.isEmpty()) {
            PowerWaveDevice device = deviceList.get(0);
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

            //预警信息
            List<AlarmWarnItemVo> alarmWarnItemVos=alarmWarnService.getDeviceAlarmInfo(device.getProjectId(),device.getDeviceId(),2000L);
            map.put("alarmData", alarmWarnItemVos);

            //进线柜下所有设备警告比例
            List<Map<String, Object>> alarmCount = alarmWarnService.getDeviceAlarmCount(device.getProjectId(),device.getDeviceId(),2000L);
            map.put("alarmCount", alarmCount);

            //电流谐波
            List<Map<String, Object>> thdiData = deviceDataSmartElecService.selectThdiData(device.getDeviceId());
            map.put("thdiData", thdiData);
            //电压谐波
            List<Map<String, Object>> thdvData = deviceDataSmartElecService.selectThdvData(device.getDeviceId());
            map.put("thdvData", thdvData);

            //电流畸变率
            BigDecimal thdiScale = new BigDecimal(dataSmartElec.getThdiA()).add(new BigDecimal(dataSmartElec.getThdiB())).add(new BigDecimal(dataSmartElec.getThdiC())).divide(new BigDecimal("3")).setScale(3);
            map.put("thdiScale", thdiScale.toString());

            BigDecimal thdvScale = new BigDecimal(dataSmartElec.getThdvA()).add(new BigDecimal(dataSmartElec.getThdvB())).add(new BigDecimal(dataSmartElec.getThdvC())).divide(new BigDecimal("3")).setScale(3);
            map.put("thdvScale", thdvScale.toString());

            BigDecimal scale = new BigDecimal("1000");
            //电流补偿谐波数据
            BigDecimal thdiCount = new BigDecimal(dataSmartElec.getAmpRmsA()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdiA()).divide(scale))
                                        .add(new BigDecimal(dataSmartElec.getAmpRmsB()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdiB()).divide(scale)))
                                        .add(new BigDecimal(dataSmartElec.getAmpRmsC()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdiC()).divide(scale)));
            map.put("thdiCount", thdiCount);
            //电压补偿谐波数据
            BigDecimal thdvCount = new BigDecimal(dataSmartElec.getVoltRmsA()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdvA()).divide(scale))
                    .add(new BigDecimal(dataSmartElec.getVoltRmsB()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdvB()).divide(scale)))
                    .add(new BigDecimal(dataSmartElec.getVoltRmsC()).divide(scale).multiply(new BigDecimal(dataSmartElec.getThdvC()).divide(scale)));
            map.put("thdvCount", thdvCount);

            //监控视频
            //监控视频
            MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(device.getDeviceId());
            if(!Objects.isNull(monitorDevice)){
                map.put("videoUrl", monitorDevice.getHlsHd());
            }
        }

        return map;
    }

    @Resource
    private MonitorDeviceService monitorDeviceService;

    @Override
    public boolean selectWaveWhetherAlarm(Long waveId) {
        return waveDeviceDao.selectWaveWhetherAlarm(waveId);
    }

    @Override
    public boolean selectWaveWhetherOffLine(Long waveId) {
        return waveDeviceDao.selectWaveWhetherOffLine(waveId);
    }

    @Override
    public void deleteByDeviceId(Long deviceId) {
        Device device=new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(deviceId);
        deviceService.update(device);
        waveDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        waveDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public PowerWave findWaveByDeviceId(Long id) {
        return waveDeviceDao.selectWaveByDeviceId(id);
    }

    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return waveDeviceDao.findByDeviceSet(projectId,value);
    }

    @Override
    public List<Long> getPowerDeviceList(Long projectId, Long powerId) {
        return waveDeviceDao.getPowerDeviceList(projectId,powerId);
    }
}
