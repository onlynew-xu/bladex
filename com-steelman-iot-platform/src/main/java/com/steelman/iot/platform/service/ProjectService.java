package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.ProjectDto;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    /**
     * 获取用户的所有项目
     * @param userId
     * @return
     */
    List<Project> getProjectsByUserId(Long userId);

    /**
     * 获取用户的所有项目信息
     * @param userId
     * @return
     */
    List<ProjectSimInfo> getProjectSimInfoByUserId(Long userId);


    void deleteProjectPicture(Project record);

    void updateElectricRoomPicture(Project record);

    List<Project> getProjectsPictureById(Long id);

    Pager<T> selectByAll(Map<String, Integer> params);

    void addProjectPicture(Project projectPicture);

    Project findById(Long projectId);

    int update(Project project);

    /**
     * 获取所有项目
     * @return
     */
    List<Project> findAll();

    /**
     * 获取可选的项目信息
     * @param userId
     * @return
     */
    List<EntityDto> getSelectInfo(Long userId);

    int saveProject(Map<String, Object> paramMap);

    Project findByProjectName(String name);

    Boolean updateProject(ProjectDto projectDto);

    int remove(Long projectId);
}
