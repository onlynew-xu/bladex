package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_data_door_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceDataDoorElec implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备主键
     */
    @ApiModelProperty(value = "设备主键")
    private Long deviceId;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String serialNum;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}