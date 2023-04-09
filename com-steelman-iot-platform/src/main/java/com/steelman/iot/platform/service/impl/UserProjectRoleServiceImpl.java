package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.UserProjectRoleDao;
import com.steelman.iot.platform.entity.UserProjectRole;
import com.steelman.iot.platform.entity.dto.UserProjectRoleDto;
import com.steelman.iot.platform.service.UserProjectRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-23
 */
@Service("userProjectRoleService")
public class UserProjectRoleServiceImpl implements UserProjectRoleService {
    @Resource
    private UserProjectRoleDao userProjectRoleDao;
    @Override
    public UserProjectRoleDto findByUserIdAndProjectId(Long userId, Long projectId) {
        return userProjectRoleDao.findByUserIdAndProjectId(userId,projectId);
    }

    @Override
    public List<String> findUserNameByProjectId(Long projectId) {
        return userProjectRoleDao.findUserNameByProjectId(projectId);
    }

    @Override
    public List<UserProjectRole> findByUserId(Long userId) {
        return userProjectRoleDao.findByUserId(userId);
    }

    @Override
    public List<UserProjectRoleDto> findDtoByUserId(Long userId) {
        return userProjectRoleDao.findDtoByUserId(userId);
    }

    @Override
    public int insertData(UserProjectRole userProjectRole) {
        return userProjectRoleDao.insert(userProjectRole);
    }

    @Override
    public int updateUserProjectRole(UserProjectRole userProjectRole) {
        return userProjectRoleDao.updateUserProjectRole(userProjectRole);
    }

    @Override
    public int remove(Long id) {
        return userProjectRoleDao.deleteByPrimaryKey(id);
    }

    @Override
    public UserProjectRole getSuperAdmin(Long projectId) {
        return userProjectRoleDao.getSuperAdmin(projectId);
    }

    @Override
    public List<UserProjectRoleDto> findDtoByProjectId(Long projectId) {
        return userProjectRoleDao.findDtoByProjectId(projectId);
    }
}
