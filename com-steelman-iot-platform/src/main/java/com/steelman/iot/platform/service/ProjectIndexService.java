package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.ProjectIndex;

public interface ProjectIndexService {

    /**
     * 保存项目登录页
     * @param projectIndex
     * @return
     */
    int saveProjectIndex(ProjectIndex projectIndex);


    /**
     * 更新项目登录页
     * @param projectIndex
     * @return
     */
    int updateIndex(ProjectIndex projectIndex);
}
