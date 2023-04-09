package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DataTemperaturehumidity;
import com.steelman.iot.platform.entity.PowerDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;

import java.util.List;
import java.util.Set;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 15:33
 * @Description:
 */
public interface PowerDeviceService {
    void insert(PowerDevice powerDevice);

    List<PowerDevice> selectByPowerId(Long id);

    void deleteByPowerId(Long id);

    List<PowerDeviceInfo> getPowerDeviceList( Long powerId);

    void delete(Long id);

    PowerDevice getInfo(Long id);

    void update(PowerDevice powerDevice);

    DataTemperaturehumidity selectPowerTemperatureHumidity(Long powerId);

    PowerDevice getInfoByDeviceId(Long deviceId);


    /**
     * 删除设备的关联关系
     * @param deviceId
     */
    void deleteByDeviceId(Long deviceId);

    void removeDevice(Long deviceId);

    String findPowerNameByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(Long projectId, Set<Long> value);
}
