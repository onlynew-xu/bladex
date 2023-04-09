package com.steelman.iot.platform.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author tang
 * date 2020-11-20
 */
@Component
public class EncodePasswordUtils {

    @Resource
    private PasswordEncoder passwordEncoder;

    public  String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
