package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.AlarmType;

/**
 * @Author lsj
 * @DATE 2021/4/22 0022 10:02
 * @Description:
 */
public interface AlarmTypeService {
    AlarmType findById(Long alarmTypeId);
}
