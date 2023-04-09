package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.FactoryProcessUnitDao;
import com.steelman.iot.platform.entity.FactoryProcessUnit;
import com.steelman.iot.platform.service.FactoryProcessUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("factoryProcessUnitService")
public class FactoryProcessUnitServiceImpl extends BaseService implements FactoryProcessUnitService {
    @Resource
    private FactoryProcessUnitDao factoryProcessUnitDao;
    @Override
    public FactoryProcessUnit getByFactoryId(Long factoryInfoId) {
        return factoryProcessUnitDao.getByFactoryId(factoryInfoId);
    }
}
