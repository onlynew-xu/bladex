package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerFeederLoopDao;
import com.steelman.iot.platform.entity.PowerFeederLoop;
import com.steelman.iot.platform.service.PowerFeederLoopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/17 0017 17:06
 * @Description:
 */
@Service("powerFeederLoopService")
public class PowerFeederLoopServiceImpl extends BaseService implements PowerFeederLoopService {

    @Resource
    private PowerFeederLoopDao feederLoopDao;

    @Override
    public void insert(PowerFeederLoop feederLoop) {
        feederLoopDao.insert(feederLoop);
    }

    @Override
    public void update(PowerFeederLoop feederLoop) {
        feederLoopDao.updateByPrimaryKeySelective(feederLoop);
    }

    @Override
    public PowerFeederLoop getFeederLoopInfo(Long feederLoopId) {
        return feederLoopDao.selectByPrimaryKey(feederLoopId);
    }

    @Override
    public  List<PowerFeederLoop> getFeederLoopByFeederId(Long feederId) {
        List<PowerFeederLoop> list = feederLoopDao.selectLoopByFeederId( feederId);
        return list;
    }

    @Override
    public PowerFeederLoop findByFeederIdAndLoop(Long feederId, String loopName) {
        return feederLoopDao.findByFeederIdAndLoop(feederId,loopName);
    }

    @Override
    public int removeFeederLoop(Long id) {
        return feederLoopDao.deleteByPrimaryKey(id);
    }

    @Override
    public int removeByFeederId(Long feederId) {
        return feederLoopDao.deleteByFeederId(feederId);
    }

    @Override
    public List<PowerFeederLoop> getLoopList(Long feederId) {
        return feederLoopDao.getLoopList(feederId);
    }
}
