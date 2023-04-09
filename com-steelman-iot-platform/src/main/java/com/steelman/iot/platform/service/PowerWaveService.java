package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerWave;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.entity.dto.WaveDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/13 0013 17:55
 * @Description:
 */
public interface PowerWaveService {
    void insert(PowerWave wave);

    void update(PowerWave wave);

    PowerWave getWaveInfo(Long waveId);

    Long selectCountByPowerId(Long powerId);

    List<PowerWave> selectByTransformerId(Long transformerId);

    List<DeviceCenterVo> selectPowerWave(Long powerId);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerWave selectByName(Long transformerId, String waveName);

    int deleteById(Long waveId);

    /**
     * 获取所有元器件
     * @param waveId
     * @param type
     * @return
     */
    List<PowerComponentsDto> getComponents(Long waveId, Integer type);

    /**
     * 获取电房中所有滤波
     * @param powerId
     * @return
     */
    List<PowerWave> findByPowerId(Long powerId);

    /**
     * 补偿柜详情页
     * @param waveId
     * @return
     */
    WaveDto getWaveInfoDto(Long waveId);


}
