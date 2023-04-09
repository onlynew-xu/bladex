package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerBoxLoopDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.PowerBoxLoopDevice;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 15:22
 * @Description:
 */
@Service("powerBoxLoopDeviceService")
public class PowerBoxLoopDeviceServiceImpl extends BaseService implements PowerBoxLoopDeviceService {
    @Resource
    private PowerBoxLoopDeviceDao loopDeviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private PowerBoxLoopService powerBoxLoopService;

    @Override
    public void insert(PowerBoxLoopDevice loopDevice) {
        loopDeviceDao.insert(loopDevice);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceList(Long loopId) {
        return loopDeviceDao.selectByLoopId(loopId);
    }

    @Override
    public void delete(Long id) {
        PowerBoxLoopDevice boxLoopDevice = loopDeviceDao.selectByPrimaryKey(id);
        Device device = new Device();
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        device.setId(boxLoopDevice.getDeviceId());
        deviceService.update(device);
        loopDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerBoxLoopDevice getInfo(Long id) {
        return loopDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(PowerBoxLoopDevice boxLoopDevice) {
        loopDeviceDao.updateByPrimaryKeySelective(boxLoopDevice);
    }

    @Override
    public PowerBoxLoopDevice getInfoByDeviceId(Long deviceId) {
        return loopDeviceDao.selectByDeviceId(deviceId);
    }

    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private MonitorDeviceService monitorDeviceService;


    @Override
    public Map<String, Object> getBoxLoopData(Long loopId) {
        Map<String, Object> map = new HashMap<>();
        List<PowerBoxLoopDevice> deviceList = loopDeviceDao.selectLoopDevice(loopId);
        PowerBoxLoopDevice loopDevice = deviceList.get(0);

        //实时数据
        DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(loopDevice.getDeviceId());
        map.put("realData", dataSmartElec);
        //电流
        List<Map<String, Object>> ampData = deviceDataSmartElecService.selectPowerAmpData(loopDevice.getDeviceId());
        map.put("ampData", ampData);
        //电压
        List<Map<String, Object>> voltData = deviceDataSmartElecService.selectPowerVoltData(loopDevice.getDeviceId());
        map.put("voltData", voltData);
        //功率因数
        List<Map<String, Object>> factorData = deviceDataSmartElecService.selectPowerFactorData(loopDevice.getDeviceId());
        map.put("factorData", factorData);
        //有功功率
        List<Map<String, Object>> activeData = deviceDataSmartElecService.selectPowerActiveData(loopDevice.getDeviceId());
        map.put("activeData", activeData);
        //无功功率
        List<Map<String, Object>> reactiveData = deviceDataSmartElecService.selectPowerReactiveData(loopDevice.getDeviceId());
        map.put("reactiveData", reactiveData);
        //有功电度
        List<Map<String, Object>> degreeData = deviceMeasurementService.getLastSevenDayTotal(loopDevice.getDeviceId());
        map.put("degreeData", degreeData);

        //预警信息
        List<AlarmWarnItemVo> alarmWarnItemVos = alarmWarnService.getDeviceAlarmInfo(loopDevice.getProjectId(), loopDevice.getDeviceId(), 2000L);
        map.put("alarmData", alarmWarnItemVos);

        //进线柜下设备警告比例
        List<Map<String, Object>> alarmCount = alarmWarnService.getDeviceAlarmCount(loopDevice.getProjectId(), loopDevice.getDeviceId(), 2000L);
        map.put("alarmCount", alarmCount);

        //监控视频
        MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(loopDevice.getDeviceId());
        if(!Objects.isNull(monitorDevice)){
            map.put("videoUrl", monitorDevice.getHlsHd());
        }

        //实时数据总和
        Map<String, Object> realDataCount = new HashMap<>();
        BigDecimal voltCountA = new BigDecimal("0");
        BigDecimal voltCountB = new BigDecimal("0");
        BigDecimal voltCountC = new BigDecimal("0");

        BigDecimal ampA = new BigDecimal("0");
        BigDecimal ampB = new BigDecimal("0");
        BigDecimal ampC = new BigDecimal("0");

        BigDecimal activePowerA = new BigDecimal("0");
        BigDecimal activePowerB = new BigDecimal("0");
        BigDecimal activePowerC = new BigDecimal("0");

        BigDecimal reactivePowerA = new BigDecimal("0");
        BigDecimal reactivePowerB = new BigDecimal("0");
        BigDecimal reactivePowerC = new BigDecimal("0");

        BigDecimal powerFactorA = new BigDecimal("0");
        BigDecimal powerFactorB = new BigDecimal("0");
        BigDecimal powerFactorC = new BigDecimal("0");

        for (PowerBoxLoopDevice device : deviceList) {
            DeviceDataSmartElec deviceDataSmartElec = deviceDataSmartElecService.getLastData(device.getDeviceId());
            voltCountA = voltCountA.add(new BigDecimal(deviceDataSmartElec.getVoltRmsA()));
            voltCountB = voltCountB.add(new BigDecimal(deviceDataSmartElec.getVoltRmsB()));
            voltCountC = voltCountC.add(new BigDecimal(deviceDataSmartElec.getVoltRmsC()));

            ampA = ampA.add(new BigDecimal(deviceDataSmartElec.getAmpRmsA()));
            ampB = ampB.add(new BigDecimal(deviceDataSmartElec.getAmpRmsB()));
            ampC = ampC.add(new BigDecimal(deviceDataSmartElec.getAmpRmsC()));

            activePowerA = activePowerA.add(new BigDecimal(deviceDataSmartElec.getActivePowerA()));
            activePowerB = activePowerB.add(new BigDecimal(deviceDataSmartElec.getActivePowerB()));
            activePowerC = activePowerC.add(new BigDecimal(deviceDataSmartElec.getActivePowerC()));

            reactivePowerA = reactivePowerA.add(new BigDecimal(deviceDataSmartElec.getReactivePowerA()));
            reactivePowerB = reactivePowerB.add(new BigDecimal(deviceDataSmartElec.getReactivePowerB()));
            reactivePowerC = reactivePowerC.add(new BigDecimal(deviceDataSmartElec.getReactivePowerC()));

            powerFactorA = powerFactorA.add(new BigDecimal(deviceDataSmartElec.getPowerFactorA()));
            powerFactorB = powerFactorB.add(new BigDecimal(deviceDataSmartElec.getPowerFactorB()));
            powerFactorC = powerFactorC.add(new BigDecimal(deviceDataSmartElec.getPowerFactorC()));

        }
        realDataCount.put("voltCountA", voltCountA);
        realDataCount.put("voltCountB", voltCountB);
        realDataCount.put("voltCountC", voltCountC);
        realDataCount.put("ampA", ampA);
        realDataCount.put("ampB", ampB);
        realDataCount.put("ampC", ampC);
        realDataCount.put("activePowerA", activePowerA);
        realDataCount.put("activePowerB", activePowerB);
        realDataCount.put("activePowerC", activePowerC);

        realDataCount.put("reactivePowerA", reactivePowerA);
        realDataCount.put("reactivePowerB", reactivePowerB);
        realDataCount.put("reactivePowerC", reactivePowerC);

        realDataCount.put("powerFactorA", powerFactorA);
        realDataCount.put("powerFactorB", powerFactorB);
        realDataCount.put("powerFactorC", powerFactorC);
        map.put("realDataCount", realDataCount);
        return map;
    }

    @Override
    public boolean selectBoxWhetherAlarm(Long boxId) {
        return loopDeviceDao.selectBoxWhetherAlarm(boxId);
    }

    @Override
    public boolean selectBoxWhetherOffLine(Long boxId) {
        return loopDeviceDao.selectBoxWhetherOffLine(boxId);
    }

    @Override
    public void removeByDeviceId(Long deviceId) {
        Device device = new Device();
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
    public Boolean bindingDevice(Long boxLoopId, Long deviceId) {
        Device deviceOld = deviceService.findById(deviceId);
        if(deviceOld.getBindingStatus().equals(1)){
            return false;
        }
        PowerBoxLoop powerBoxLoop = powerBoxLoopService.getBoxLoopInfo(boxLoopId);
        //添加设备绑定回路
        PowerBoxLoopDevice powerBoxLoopDevice=new PowerBoxLoopDevice();
        powerBoxLoopDevice.setDeviceId(deviceId);
        powerBoxLoopDevice.setBoxId(powerBoxLoop.getBoxId());
        powerBoxLoopDevice.setBoxLoopId(boxLoopId);
        powerBoxLoopDevice.setPowerId(powerBoxLoop.getPowerId());
        powerBoxLoopDevice.setProjectId(powerBoxLoop.getProjectId());
        powerBoxLoopDevice.setCreateTime(new Date());
        powerBoxLoopDevice.setUpdateTime(powerBoxLoopDevice.getCreateTime());
        loopDeviceDao.insert(powerBoxLoopDevice);
        //添加设备绑定状态
        Device device=new Device();
        device.setId(deviceId);
        device.setBindingStatus(1);
        device.setBindingType(5);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindingDevice(Long boxLoopDeviceId, Long deviceId) {
        //释放设备绑定
        Device device=new Device();
        device.setId(deviceId);
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        loopDeviceDao.deleteByPrimaryKey(boxLoopDeviceId);
        return true;
    }

    @Override
    public PowerBoxLoop findBoxLoopByDeviceId(Long id) {
        return loopDeviceDao.selectBoxLoopByDeviceId(id);
    }

    @Override
    public List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value) {
        return loopDeviceDao.findByDeviceSet(projectId,value);
    }

    @Override
    public List<PowerDeviceInfo> getDeviceByBoxId(Long boxId) {
        return loopDeviceDao.getDeviceByBoxId(boxId);
    }

    @Override
    public List<Long> getPowerDeviceList(Long projectId, Long powerId) {
        return loopDeviceDao.getPowerDeviceList(projectId,powerId);
    }
}
