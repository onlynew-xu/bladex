package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_energy_type_picture
 * @author 
 */
@Data
public class EnergyTypePicture implements Serializable {
    private Long id;

    /**
     * 能源设备类型
     */
    private Long energyTypeId;

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