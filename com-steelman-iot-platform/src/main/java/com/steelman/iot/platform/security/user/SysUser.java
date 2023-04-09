package com.steelman.iot.platform.security.user;

import com.steelman.iot.platform.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


/**
 * @author tang
 * date 2020-11-20
 */
public class SysUser  implements UserDetails {

    private transient User currentUser;

    private Collection<? extends GrantedAuthority> authorities;

    public SysUser(User user) {
        if (user != null) {
            this.currentUser = user;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return currentUser.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
