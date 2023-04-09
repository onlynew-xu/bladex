package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_device_params_temperatureHumidity
 * @author 
 */
@Data
public class ParamsTemperaturehumidity implements Serializable {
    private Long id;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String serialNum;

    /**
     * 心跳上报延时时间,单位为秒
     */
    private String interval;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}