package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_monitor_device
 * @author 
 */
@Data
public class MonitorDevice implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 监控Id
     */
    private Long monitorId;

    /**
     * 终端设备Id
     */
    private Long deviceId;

    /**
     * 播放地址
     */
    private String hlsHd;

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