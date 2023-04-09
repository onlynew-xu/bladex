package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerBoxLoop;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 10:03
 * @Description:
 */
public interface PowerBoxLoopService {
    void insert(PowerBoxLoop boxLoop);

    void update(PowerBoxLoop loop);

    PowerBoxLoop getBoxLoopInfo(Long boxLoopId);

    List<PowerBoxLoop> getBoxLoop(Long boxId);

    int removeByBoxId(Long boxId);

    PowerBoxLoop getBoxLoopInfoByBoxAndName(Long boxId, String loopName);

    int removeByLoopId(Long boxLoopId);

    List<PowerBoxLoop> getBoxLoopList(Long boxId);
}
