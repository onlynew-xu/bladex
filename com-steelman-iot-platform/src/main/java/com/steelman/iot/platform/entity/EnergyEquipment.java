package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_energy_equipment
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class EnergyEquipment implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 额定功率
     */
    @ApiModelProperty(value = "额定功率")
    private String ratedPower;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date productionDate;

    /**
     * 使用公司
     */
    @ApiModelProperty(value = "使用公司")
    private Long companyId;

    /**
     * 区域Id
     */
    @ApiModelProperty(value = "区域Id")
    private Long areaId;

    /**
     * 楼Id
     */
    @ApiModelProperty(value = "楼Id")
    private Long buildingId;

    /**
     * 楼层Id
     */
    @ApiModelProperty(value = "楼层Id")
    private Long storeyId;

    /**
     * 房间号Id
     */
    @ApiModelProperty(value = "房间号Id")
    private Long roomId;

    /**
     * 自定义设备位置
     */
    @ApiModelProperty(value = "自定义设备位置")
    private String location;

    /**
     * 能源设备类型
     */
    @ApiModelProperty(value = "能源设备类型")
    private Long energyTypeId;

    /**
     * 能耗设备类型
     */
    @ApiModelProperty(value = "能耗设备类型")
    private Long consumeTypeId;

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