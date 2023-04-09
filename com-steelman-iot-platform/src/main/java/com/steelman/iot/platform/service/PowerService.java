package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.entity.DeviceDataSmartSuper;
import com.steelman.iot.platform.entity.Power;
import com.steelman.iot.platform.entity.PowerPicture;
import com.steelman.iot.platform.entity.dto.DeviceDataSmartElecFloatDto;
import com.steelman.iot.platform.entity.dto.PowerCenterInfo;
import com.steelman.iot.platform.entity.dto.PowerDataSimple;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.PowerDataInfo;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceCount;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceStatus;
import com.steelman.iot.platform.largescreen.vo.PowerRecentInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 10:28
 * @Description:
 */
public interface PowerService {
    void insert(Power power);

    List<Power> selectByProjectId(Long projectId);

    void deleteById(Long id);

    void update(Power power);

    Power getPowerInfo(Long powerId);

    List<PowerDataVo> getPowerData(Long projectId);

//    List<PowerInfoVo> getPowerDataInfo(Long powerId);

    Power selectByNameAndProjectId(String powerName, Long projectId);

    List<PowerDataSimple> getPowerSimple(Long projectId);

    PowerInfoVo getPowerDetail( Long powerId);

    PowerCenterInfo getPowerCenter(Long projectId);

    List<PowerFacilityCenterInfo> getPowerCenterDevice(Long powerId);

    /**
     * 获取滚动的信息
     * @param powerId
     * @param deviceId
     * @return
     */
     List<PowerAlarmWarnVo> getMessage(Long powerId, Long deviceId, Integer type, Long id,Long loopId,String loopName);

    /**
     * 获取告警统计
     * @param type
     * @param id
     * @return
     */
    List<DeviceTypeAlarmStatistic> alarmStatistic(Integer type, Long id,Object loopIdObj);

    /**
     * 获取告警历史信息
     * @param type
     * @param id
     * @return
     */
    List<AlarmWarnItemVo> alarmHistory(Integer type, Long id,Object loopObj);

    String getLocation(Long deviceId);

    DeviceDataSmartElecFloatDto getFloatData(DeviceDataSmartElec deviceDataSmartElec);

    DeviceDataSmartElecFloatDto getSuperFloatData(DeviceDataSmartSuper deviceDataSmartSuper);

    Map<String, String[][]> getAlarmDownLoadData(Long projectId);

    /**
     * 获取电房中的设备数量
     * @param projectId
     * @return
     */
    PowerDeviceCount getPowerCountInfo(Long projectId);

    /**
     * 获取设备的在线数量
     * @param projectId
     * @return
     */
    PowerDeviceStatus getPowerDeviceStatus(Long projectId);

    /**
     * 获取电房的温度和湿度信息
     * @param projectId
     * @return
     */
    List<PowerDataInfo> getPowerDataInfo(Long projectId);


    /**
     * 获取指定电房的告警类型占比
     * @param projectId
     * @param powerId
     * @return
     */
    List<DeviceTypeAlarmStatistic> getPowerAlarmStatistic(Long projectId, Long powerId);

    /**
     * 获取电房各电房设备的数量
     * @param projectId
     * @param powerId
     * @return
     */
    List<DeviceTypeAlarmStatistic> getPowerEquipment(Long projectId, Long powerId);

    /**
     * 获取电房的数据
     * @param powerId
     * @return
     */
    PowerRecentInfo getPowerBaseInfo(Long powerId);

    /**
     * 获取电房的图片信息
     * @param powerId
     * @return
     */
    PowerPicture getPowerPicture(Long powerId);
}
