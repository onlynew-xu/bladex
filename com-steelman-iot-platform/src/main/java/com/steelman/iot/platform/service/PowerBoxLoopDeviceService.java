package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerBoxLoop;
import com.steelman.iot.platform.entity.PowerBoxLoopDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 15:22
 * @Description:
 */
public interface PowerBoxLoopDeviceService {
    void insert(PowerBoxLoopDevice loopDevice);

    List<PowerDeviceInfo> getDeviceList(Long loopId);

    void delete(Long id);

    PowerBoxLoopDevice getInfo(Long id);

    void update(PowerBoxLoopDevice boxLoopDevice);

    PowerBoxLoopDevice getInfoByDeviceId(Long deviceId);

    Map<String, Object> getBoxLoopData(Long loopId);

    boolean selectBoxWhetherAlarm(Long boxId);

    boolean selectBoxWhetherOffLine(Long boxId);

    void removeByDeviceId(Long deviceId);

    void removeDevice(Long deviceId);

    Boolean bindingDevice(Long boxLoopId, Long deviceId);

    Boolean unbindingDevice(Long boxLoopDeviceId, Long deviceId);

    PowerBoxLoop findBoxLoopByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);

    List<PowerDeviceInfo> getDeviceByBoxId(Long powerId);

    List<Long> getPowerDeviceList(Long projectId, Long powerId);
}
