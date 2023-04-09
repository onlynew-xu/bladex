package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerFeederLoop;
import com.steelman.iot.platform.entity.PowerFeederLoopDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 15:52
 * @Description:
 */
public interface PowerFeederLoopDeviceService {
    void insert(PowerFeederLoopDevice feederDevice);

    List<PowerDeviceInfo> getDeviceList(Long loopId);

    void delete(Long id);

    PowerFeederLoopDevice getInfo(Long id);

    void update(PowerFeederLoopDevice loopDevice);

    PowerFeederLoopDevice getInfoByDeviceId(Long deviceId);

    Map<String, Object> getPowerFeederLoopDeviceData(Long loopId);

    boolean selectFeederWhetherAlarm(Long feederId);

    boolean selectFeederWhetherOffLine(Long feederId);


    void removeByDeviceId(Long deviceId);

    void removeDevice(Long deviceId);

    Boolean unbindingDevice(Long feederLoopDeviceId, Long deviceId);

    Boolean bindingDevice(Long feederLoopId, Long deviceId);

    PowerFeederLoop findFeederLoopByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);

    List<PowerDeviceInfo> getDeviceByFeederId(Long feederId);

    List<Long> getPowerDeviceList(Long projectId, Long powerId);
}
