package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.dto.IncomingData;
import com.steelman.iot.platform.entity.dto.IncomingDto;
import com.steelman.iot.platform.entity.dto.IncomingInfo;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.entity.vo.WeekEnergyConsumeStatistic;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/12 0012 15:08
 * @Description:
 */
public interface PowerIncomingService {
    void insert(PowerIncoming powerIncoming);

    void update(PowerIncoming powerIncoming);

    List<PowerIncoming> getIncomingList(Long transformerId);

    void delete(Long id);

    PowerIncoming getIncomingInfo(Long id);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> getPowerIncoming(Long id);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerIncoming selectByName(String incomingName, Long transformerId);

    List<PowerIncoming> findByPowerId(Long powerId);

    List<PowerIncoming> getByTransformerId(Long transformerId);

    IncomingDto getIncomingDto(Long incomingId);

    IncomingData getIncomingData(Long incomingId);

    Map<String, List<WeekEnergyConsumeStatistic>> getMeasureData(Long incomingId);

    IncomingInfo getIncomingInfoDetail(Long incomingId);
}
