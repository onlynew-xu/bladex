package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.ProjectSystem;
import com.steelman.iot.platform.entity.vo.ProjectSystemDetail;

import java.util.List;

public interface ProjectSystemService {
    /**
     * 获取项目的所有系统
     * @param projectId
     * @return
     */
    List<ProjectSystemDetail> getAllActiveSystem(Long projectId);

    List<ProjectSystemDetail> getAllSystem(Long projectId);

    int updateStatus(Long projectId, Long systemId, Integer status);

    /**
     * 保存项目系统
     * @param projectId
     * @param systemIds
     * @return
     */
    int saveSystemIds(Long projectId, String systemIds);

    /**
     * 删除项目系统
     * @param id
     * @return
     */
    int removeProjectSystem(Long id);

    /**
     * 获取项目系统
     * @param projectId
     * @return
     */
    List<ProjectSystem> projectSystemInfo(Long projectId);
}
