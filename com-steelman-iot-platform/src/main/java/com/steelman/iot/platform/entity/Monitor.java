package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_monitor
 * @author 
 */
@Data
public class Monitor implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备序列号
     */
    private String serialNum;

    /**
     * 设备验证码
     */
    private String validateCode;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 播放地址
     */
    private String hls;

    /**
     * 播放地址
     */
    private String hlsHd;

    /**
     * 播放地址
     */
    private String rtmp;

    /**
     * 播放地址
     */
    private String rtmpHd;

    /**
     * 项目Id
     */
    private Long projectId;

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