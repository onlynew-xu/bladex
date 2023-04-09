package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerBoxLoopDao;
import com.steelman.iot.platform.entity.PowerBoxLoop;
import com.steelman.iot.platform.service.PowerBoxLoopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 10:03
 * @Description:
 */
@Service("powerBoxLoopService")
public class PowerBoxLoopServiceImpl extends BaseService implements PowerBoxLoopService {

    @Resource
    private PowerBoxLoopDao boxLoopDao;

    @Override
    public void insert(PowerBoxLoop boxLoop) {
        boxLoopDao.insert(boxLoop);
    }

    @Override
    public void update(PowerBoxLoop loop) {
        boxLoopDao.updateByPrimaryKeySelective(loop);
    }

    @Override
    public PowerBoxLoop getBoxLoopInfo(Long boxLoopId) {
        return boxLoopDao.selectByPrimaryKey(boxLoopId);
    }

    @Override
    public List<PowerBoxLoop> getBoxLoop(Long boxId) {

        return boxLoopDao.selectBoxLoop(boxId);
    }

    @Override
    public int removeByBoxId(Long boxId) {
        return boxLoopDao.deleteByBoxId(boxId);
    }

    @Override
    public PowerBoxLoop getBoxLoopInfoByBoxAndName(Long boxId, String loopName) {
        return boxLoopDao.getBoxLoopInfoByBoxAndName(boxId,loopName);
    }

    @Override
    public int removeByLoopId(Long boxLoopId) {
        return boxLoopDao.deleteByPrimaryKey(boxLoopId);
    }

    @Override
    public List<PowerBoxLoop> getBoxLoopList(Long boxId) {
        return boxLoopDao.getBoxLoopList(boxId);
    }
}
