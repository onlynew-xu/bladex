package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_picture
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DevicePicture implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String serialNum;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    /**
     * 图片url
     */
    @ApiModelProperty(value = "图片url")
    private String url;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}