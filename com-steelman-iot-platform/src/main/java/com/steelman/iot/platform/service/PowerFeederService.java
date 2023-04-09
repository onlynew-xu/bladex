package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerFeeder;
import com.steelman.iot.platform.entity.dto.PowerFeederInfoDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 14:09
 * @Description:
 */
public interface PowerFeederService {
    void insert(PowerFeeder feeder);

    void update(PowerFeeder feeder);

    List<PowerFeeder> getFeederList(Integer loopType, Long transformerId) ;

    PowerFeeder getFeederInfo(Long feederId);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> getPowerFeeder(Long powerId, int loopType);

    Long selectSimpleCountByTransformId(Long transformId, Long projectId);

    Long selectMultiCountByTransformId(Long transformId, Long projectId);

    List<PowerFeeder> getPowerFeederType(Long transformerId, Integer type);

    Boolean save(Long transformerId, String feederName, int type);

    PowerFeeder selectByPowerIdAndName(String feederName, Long powerId);

    int removeFeeder(Long feederId);

    List<PowerFeeder> findByPowerId(Long powerId);

    PowerFeederInfoDto getSimpleFeederInfo(Long feederId);

    PowerFeederInfoDto getMultiplyFeederInfo(Long feederId, Long loopId);
}
