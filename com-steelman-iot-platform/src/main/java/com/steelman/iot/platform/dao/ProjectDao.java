package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface ProjectDao {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    /**
     * 获取用户的所有项目
     * @param userId
     * @return
     */
    List<Project> getProjectsByUserId(@Param("userId") Long userId);

    /**
     * 获取用户项目的概略信息
     * @param userId
     * @return
     */
    List<ProjectSimInfo> getProjectSimInfoByUserId(@Param("userId") Long userId);

    void deletePictureByid(Project record);

    List<Project> getProjectsPictureById(Long id);

    List<T> selectByAll(int page, int size);

    void addProjectPicture(Project projectPicture);

    List<Project> findAll();

    Project findByProjectName(@Param("projectName") String name);
}