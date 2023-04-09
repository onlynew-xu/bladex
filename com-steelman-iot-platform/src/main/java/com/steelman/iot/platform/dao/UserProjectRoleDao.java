package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.UserProjectRole;
import com.steelman.iot.platform.entity.dto.UserProjectRoleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProjectRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(UserProjectRole record);

    int insertSelective(UserProjectRole record);

    UserProjectRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserProjectRole record);

    int updateByPrimaryKey(UserProjectRole record);

    /**
     * 获取用户项目的权限
     * @param userId
     * @param projectId
     * @return
     */
    UserProjectRoleDto findByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    List<String> findUserNameByProjectId(Long projectId);

    List<UserProjectRole> findByUserId(@Param("userId") Long userId);

    List<UserProjectRoleDto> findDtoByUserId(@Param("userId") Long userId);

    int updateUserProjectRole(UserProjectRole userProjectRole);

    /**
     * 获取项目的超级管理员
     * @param projectId
     * @return
     */
    UserProjectRole getSuperAdmin(@Param("projectId") Long projectId);

    List<UserProjectRoleDto> findDtoByProjectId(@Param("projectId") Long projectId);
}