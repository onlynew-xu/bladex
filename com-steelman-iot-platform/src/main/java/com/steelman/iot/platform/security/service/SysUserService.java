package com.steelman.iot.platform.security.service;

import com.steelman.iot.platform.dao.UserDao;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.security.user.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tang
 * date 2020-11-20
 */
@Service("sysUserService")
public class SysUserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.selectByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        SysUser sysUser=new SysUser(user);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        sysUser.setAuthorities(authorities);
        return sysUser;
    }

}
