package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_type_picture
 * @author 
 */
@Data
public class PowerTypePicture implements Serializable {
    private Long id;

    /**
     * 电房设备类型 0:变压器 1:发电机 2:进线柜 3:补偿柜 4:滤波柜 5:单回路馈线柜 6:多回路馈线柜 7:配电箱 8:母联柜
     */
    private Integer powerTypeId;

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