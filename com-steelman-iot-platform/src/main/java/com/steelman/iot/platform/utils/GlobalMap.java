package com.steelman.iot.platform.utils;

import com.steelman.iot.platform.entity.User;

import java.util.HashMap;
import java.util.Map;


/**
 * 全局MAP
 *
 */
public class GlobalMap {
	
	//全局用户map key 为token value为用户
	public static Map<String, User> userMap = new HashMap<String, User>();

}
