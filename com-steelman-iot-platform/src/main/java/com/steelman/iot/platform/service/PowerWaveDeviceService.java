package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerWave;
import com.steelman.iot.platform.entity.PowerWaveDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 10:49
 * @Description:
 */
public interface PowerWaveDeviceService {
    void insert(PowerWaveDevice waveDevice);

    List<PowerDeviceInfo> getDeviceList( Long waveId);

    void delete(Long id);

    PowerWaveDevice getInfo(Long id);

    void update(PowerWaveDevice waveDevice);

    PowerWaveDevice getInfoByDeviceId(Long deviceId);

    Map<String, Object> getPowerWaveDeviceData(Long waveId);

    boolean selectWaveWhetherAlarm(Long waveId);

    boolean selectWaveWhetherOffLine(Long waveId);

    void deleteByDeviceId(Long deviceId);

    void removeDevice(Long deviceId);

    PowerWave findWaveByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);

    List<Long> getPowerDeviceList(Long projectId, Long powerId);
}
