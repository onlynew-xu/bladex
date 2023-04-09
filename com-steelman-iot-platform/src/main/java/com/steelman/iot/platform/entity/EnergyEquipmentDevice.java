package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_energy_equipment_device
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class EnergyEquipmentDevice implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 能源设备Id
     */
    @ApiModelProperty(value = "能源设备Id")
    private Long equipmentId;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private Long deviceTypeId;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 生成时间
     */
    @ApiModelProperty(value = "生成时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}