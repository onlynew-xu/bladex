package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.SystemContentDao;
import com.steelman.iot.platform.entity.SystemContent;
import com.steelman.iot.platform.service.SystemContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("systemContentService")
public class SystemContentServiceImpl extends BaseService implements SystemContentService {
    @Resource
    private SystemContentDao systemContentDao;

    @Override
    public int save(SystemContent systemContent) {
        return systemContentDao.insert(systemContent);
    }

    @Override
    public int updateById(SystemContent systemContent) {
        return systemContentDao.updateByPrimaryKeySelective(systemContent);
    }

    @Override
    public SystemContent getByProjectIdAndSystemId(Long projectId, Long systemId) {
        return systemContentDao.getByProjectIdAndSystemId(projectId,systemId);
    }
}
