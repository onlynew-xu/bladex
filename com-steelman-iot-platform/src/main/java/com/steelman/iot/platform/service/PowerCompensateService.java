package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.dto.CompensateDto;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.entity.dto.PowerEntityDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/15 0015 14:39
 * @Description:
 */
public interface PowerCompensateService {
    void insert(PowerCompensate compensate);

    void update(PowerCompensate compensate);

    PowerCompensate getCompensateInfo(Long compensateId);

    List<PowerCompensate> selectByTransformerId( Long transformerId);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> getPowerCompensate(Long id);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerCompensate selectByTransformerAndName(String compensateName, Long transformerId);

    int remove(Long compensateId);

    List<PowerComponentsDto> getComponents(Long compensateId, Integer type);

    List<PowerCompensate> findByPowerId(Long powerId);

    /**
     * 获取补偿柜详情页数据
     * @param compensateId
     * @return
     */
    CompensateDto getCompensateInfoDetail(Long compensateId);

    List<PowerEntityDto> getCompensateComponentsInfo(Long compensateId);
}
