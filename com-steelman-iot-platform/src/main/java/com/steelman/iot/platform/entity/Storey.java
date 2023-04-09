package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_region_storey
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class Storey implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域")
    private Long areaId;

    /**
     * 所属楼
     */
    @ApiModelProperty(value = "所属楼")
    private Long buildingId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String picture;

    /**
     * cad图片路径
     */
    @ApiModelProperty(value = "cad图片路径")
    private String cadpicture;

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

    /**
     * 合并查询
     */
    @ApiModelProperty(value = "合并后名称")
    private String contName;


}