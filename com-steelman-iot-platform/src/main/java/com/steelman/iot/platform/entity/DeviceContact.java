package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_contact
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceContact implements Serializable {
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
     * 联系人Id
     */
    @ApiModelProperty(value = "联系人Id")
    private Long contactId;

    /**
     * 告警消息通知(0：关闭 1：开启)
     */
    @ApiModelProperty(value = "告警消息通知(0：关闭 1：开启)")
    private Integer notice;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

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