package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Role;

import java.util.List;

public interface RoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 获取所有角色
     * @return
     */
    List<Role> findAll();
}