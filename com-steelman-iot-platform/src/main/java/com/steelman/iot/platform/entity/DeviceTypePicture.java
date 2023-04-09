package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_device_type_picture
 * @author 
 */
@Data
public class DeviceTypePicture implements Serializable {
    private Long id;

    /**
     * 移动设备类型
     */
    private Long deviceTypeId;

    /**
     * 图片url
     */
    private String url;

    /**
     * 文件名
     */
    private String fileName;

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