package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.RoleDao;
import com.steelman.iot.platform.entity.Role;
import com.steelman.iot.platform.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleServiceImpl extends BaseService implements RoleService {

    @Resource
    private RoleDao roleDao;

    public  List<Role> getRoleList(){
        List<Role> roleList=roleDao.findAll();
        return roleList;
    }
}
