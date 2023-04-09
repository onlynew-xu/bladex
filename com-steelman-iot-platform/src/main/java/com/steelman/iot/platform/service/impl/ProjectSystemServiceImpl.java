package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.ProjectSystemDao;
import com.steelman.iot.platform.entity.ProjectSystem;
import com.steelman.iot.platform.entity.vo.ProjectSystemDetail;
import com.steelman.iot.platform.service.ProjectSystemService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("projectSystemService")
public class ProjectSystemServiceImpl extends BaseService implements ProjectSystemService {

    @Resource
    private ProjectSystemDao projectSystemDao;

    @Override
    public List<ProjectSystemDetail> getAllActiveSystem(Long projectId) {
        return projectSystemDao.getAllActiveSystem(projectId);
    }

    @Override
    public List<ProjectSystemDetail> getAllSystem(Long projectId) {
        return projectSystemDao.getAllSystem(projectId);
    }

    @Override
    public int updateStatus(Long projectId, Long systemId, Integer status) {
        return projectSystemDao.updateStatus(projectId,systemId,status);
    }

    @Override
    public int saveSystemIds(Long projectId, String systemIds) {
        List<ProjectSystem> projectSystemList=new ArrayList<>();
        String[] split = systemIds.split(",");
        if(split.length>0){
            Date date=new Date();
            for (String systemId : split) {
                ProjectSystem projectSystem=new ProjectSystem();
                projectSystem.setSystemId(Long.parseLong(systemId));
                projectSystem.setStatus(1);
                projectSystem.setProjectId(projectId);
                projectSystem.setCreateTime(date);
                projectSystem.setUpdateTime(date);
                projectSystemList.add(projectSystem);
            }
        }
        if(!CollectionUtils.isEmpty(projectSystemList)){
            projectSystemDao.saveList(projectSystemList);
            return 1;
        }
        return 0;
    }

    @Override
    public int removeProjectSystem(Long id) {
        return projectSystemDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ProjectSystem> projectSystemInfo(Long projectId) {
        return projectSystemDao.projectSystemInfo(projectId);
    }
}
