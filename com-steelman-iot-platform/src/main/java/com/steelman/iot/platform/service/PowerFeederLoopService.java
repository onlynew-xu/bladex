package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerFeederLoop;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 17:06
 * @Description:
 */
public interface PowerFeederLoopService {
    void insert(PowerFeederLoop feederLoop);

    void update(PowerFeederLoop feederLoop);

    PowerFeederLoop getFeederLoopInfo(Long feederLoopId);

    List<PowerFeederLoop> getFeederLoopByFeederId(Long feederId);

    PowerFeederLoop findByFeederIdAndLoop(Long feederId, String loopName);

    int removeFeederLoop( Long id);

    int removeByFeederId(Long feederId);

    List<PowerFeederLoop> getLoopList(Long feederId);
}
