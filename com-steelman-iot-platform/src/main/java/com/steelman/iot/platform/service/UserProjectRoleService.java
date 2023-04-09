package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.UserProjectRole;
import com.steelman.iot.platform.entity.dto.UserProjectRoleDto;

import java.util.List;

public interface UserProjectRoleService {
    UserProjectRoleDto findByUserIdAndProjectId(Long userId,Long projectId);

    List<String> findUserNameByProjectId(Long projectId);

    /**
     * 获取用户关联的所有项目
     * @param userId
     * @return
     */
    List<UserProjectRole> findByUserId(Long userId);

    List<UserProjectRoleDto> findDtoByUserId(Long userId);

    int insertData(UserProjectRole userProjectRole);

    /**
     * 更新用户项目权限
     * @param userProjectRole
     * @return
     */
    int updateUserProjectRole(UserProjectRole userProjectRole);

    /**
     * 删除用户项目权限
     * @param id
     * @return
     */
    int remove(Long id);

    /**
     * 查找项目的超级管理员
     * @param projectId
     * @return
     */
    UserProjectRole getSuperAdmin(Long projectId);

    /**
     * 获取项目的用户信息
     * @param projectId
     * @return
     */
    List<UserProjectRoleDto> findDtoByProjectId(Long projectId);
}
