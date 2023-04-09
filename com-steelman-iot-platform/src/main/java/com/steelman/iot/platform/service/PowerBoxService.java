package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerBox;
import com.steelman.iot.platform.entity.dto.PowerBoxInfoDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 9:56
 * @Description:
 */
public interface PowerBoxService {
    void insert(PowerBox box);

    void update(PowerBox box);

    PowerBox getBoxInfo(Long boxId);

    List<PowerBox> getBoxListByLoopId(Long loopId,  Integer parentType);

    List<PowerBox> getTransformPowerBox(Long transformerId);

    List<DeviceCenterVo> getPowerBox(Long powerId);

    PowerBox getByBoxNameAndTransformerId(String name, Long transformerId);

    int removePowerBox(Long boxId);

    Long selectCountByPowerId(Long id);

    List<PowerBox> findByPowerId(Long powerId);

    PowerBoxInfoDto getBoxInfoDto(Long boxId, Long loopId);
}
