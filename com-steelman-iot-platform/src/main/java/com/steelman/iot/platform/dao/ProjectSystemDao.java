package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.ProjectSystem;
import com.steelman.iot.platform.entity.vo.ProjectSystemDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectSystemDao {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectSystem record);

    int insertSelective(ProjectSystem record);

    ProjectSystem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectSystem record);

    int updateByPrimaryKey(ProjectSystem record);

    /**
     * 获取所有开启的项目系统
     * @param projectId
     * @return
     */
    List<ProjectSystemDetail> getAllActiveSystem(@Param("projectId") Long projectId);


    List<ProjectSystemDetail> getAllSystem(@Param("projectId") Long projectId);

    int updateStatus(@Param("projectId") Long projectId, @Param("systemId") Long systemId, @Param("status") Integer status);

    /**
     * 保存设备系统
     * @param projectSystemList
     * @return
     */
    int saveList(@Param("projectSystemList") List<ProjectSystem> projectSystemList);

    /**
     * 获取设备系统
     * @param projectId
     * @return
     */
    List<ProjectSystem> projectSystemInfo(@Param("projectId") Long projectId);
}