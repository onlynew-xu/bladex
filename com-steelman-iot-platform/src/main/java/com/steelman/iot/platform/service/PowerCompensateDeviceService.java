package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.PowerCompensateDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 14:44
 * @Description:
 */
public interface PowerCompensateDeviceService {

    void deleteByDeviceId(Long deviceId);

    void insert(PowerCompensateDevice compensateDevice);

    List<PowerDeviceInfo> getDeviceList(Long compensateId);

    void delete(Long id);

    PowerCompensateDevice getInfoByDeviceId(Long deviceId);

    Map<String, Object> getPowerCompensateDeviceData(Long compensateId);

    boolean selectCompensateWhetherAlarm(Long compensateId);

    boolean selectCompensateWhetherOffLine(Long compensateId);

    void removeDevice(Long deviceId);

    PowerCompensate findCompensateByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);

    List<Long> getPowerDeviceList(Long projectId, Long powerId);
}
