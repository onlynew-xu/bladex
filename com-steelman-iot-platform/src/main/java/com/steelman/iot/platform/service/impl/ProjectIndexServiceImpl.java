package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.ProjectIndexDao;
import com.steelman.iot.platform.entity.ProjectIndex;
import com.steelman.iot.platform.service.ProjectIndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("projectIndexService")
public class ProjectIndexServiceImpl extends BaseService implements ProjectIndexService {

    @Resource
    private ProjectIndexDao projectIndexDao;

    @Override
    public int saveProjectIndex(ProjectIndex projectIndex) {
        return projectIndexDao.insertSelective(projectIndex);
    }

    @Override
    public int updateIndex(ProjectIndex projectIndex) {
        return projectIndexDao.updateByPrimaryKeySelective(projectIndex);
    }
}
