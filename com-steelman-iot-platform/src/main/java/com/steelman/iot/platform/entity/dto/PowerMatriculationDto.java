package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.PowerMatriculation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerMatriculationDto {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 母联柜状态 0 关闭 1 打开
     */
    private Integer status;

    /**
     * 1变压器Id
     */
    private Long firstTransformerId;
    /**
     * 1变压器名称
     */
    private String firstTransformerName;
    /**
     * 1变压器品牌
     */
    private String firstTransformerBrand;
    /**
     * 1变压器容量
     */
    private String firstTransformerCapaticy;
    /**
     * 当前变压器是变压器1  0:是  1:不是
     */
    private Integer firstLocal;

    /**
     * 2变压器Id
     */
    private Long secondTransformerId;
    /**
     * 变压器2 名称
     */
    private String secondTransformerName;
    /**
     * 变压器2 品牌
     */
    private String secondTransformerBrand;

    /**
     * 变压器2 容量
     */
    private String secondTransformerCapaticy;


    /**
     * 当前变压器是变压器2  0:是  1:不是
     */
    private Integer secondLocal;

    /**
     * 配电房Id
     */
    private Long powerId;

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

    public PowerMatriculationDto(PowerMatriculation powerMatriculation){
        this.id=powerMatriculation.getId();
        this.name=powerMatriculation.getName();
        this.brand=powerMatriculation.getBrand();
        this.status=powerMatriculation.getStatus();
        this.firstTransformerId=powerMatriculation.getFirstTransformerId();
        this.firstLocal=0;
        this.secondTransformerId=powerMatriculation.getSecondTransformerId();
        this.secondLocal=0;
        this.powerId=powerMatriculation.getPowerId();
        this.projectId=powerMatriculation.getProjectId();
        this.createTime=powerMatriculation.getCreateTime();
        this.updateTime=powerMatriculation.getUpdateTime();
    }
}
