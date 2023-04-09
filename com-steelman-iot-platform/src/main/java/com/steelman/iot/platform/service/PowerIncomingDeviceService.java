package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.PowerIncomingDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 16:19
 * @Description:
 */
public interface PowerIncomingDeviceService {
    void insert(PowerIncomingDevice incomingDevice);

    void delete(Long id);

    List<PowerDeviceInfo> getDeviceList(Long incomingId);

    PowerIncomingDevice getInfo(Long id);

    void update(PowerIncomingDevice incomingDevice);

    List<Map<String, Object>> selectCountMeasurement(Long projectId);

    List<String> selectEnergyTrend(Long projectId, Integer type);

    List<String> selectLastEnergyTrend(Long projectId, Integer type);

    PowerIncomingDevice getInfoByDeviceId(Long deviceId);

    Map<String, Object> getPowerIncomingDeviceData(Long incomingId);

    boolean selectIncomingWhetherAlarm(Long incomingId);

    boolean selectIncomingWhetherOffLine(Long incomingId);



    void deleteByDeviceId(Long deviceId);

    void removeDevice(Long deviceId);

    PowerIncoming findInComingByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);

    Long getBindingStatusDeviceId(Long id);

    Long getBindingOnDevice(Long id);

    List<Long> getPowerDeviceList(Long projectId, Long powerId);
}
