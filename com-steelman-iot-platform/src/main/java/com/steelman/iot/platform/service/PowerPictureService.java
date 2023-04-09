package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerPicture;

/**
 * @author tang
 * @date 2022/5/27 下午5:40
 */
public interface PowerPictureService {


    PowerPicture getByPowerId(Long powerId);
}
