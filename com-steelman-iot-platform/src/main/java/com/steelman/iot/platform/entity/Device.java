package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class Device implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    /**
     * 模块SN码
     */
    @ApiModelProperty(value = "模块SN码")
    private String serialNum;

    /**
     * 模式:模式A,模式B
     */
    @ApiModelProperty(value = "模式:模式A,模式B")
    private String deviceModel;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private Long deviceTypeId;

    /**
     * 传输协议(0:UDP;1:MQTT)
     */
    @ApiModelProperty(value = "传输协议(0:UDP;1:MQTT)")
    private Integer transType;

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
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 消音状态 0 未消音  1 已消音
     */
    @ApiModelProperty(value = "消音状态 0 未消音  1 已消音")
    private Integer erasure;

    /**
     * 设备状态 设备通讯状态1报警 2设备主动离线 3正常 4设备下线(超时或异常问题)
     */
    @ApiModelProperty(value = "设备状态 设备通讯状态1报警 2设备主动离线 3正常 4设备下线(超时或异常问题)")
    private Integer status;

    /**
     * 开关状态 0:关闭 1:打开(防火门使用状态位)
     */
    @ApiModelProperty(value = "开关状态 0:关闭 1:打开(防火门使用状态位)")
    private Integer switchStatus;

    /**
     * 绑定状态 0未绑定 1已绑定
     */
    @ApiModelProperty(value = "绑定状态 0未绑定 1已绑定")
    private Integer bindingStatus;

    /**
     * 绑定的设备类型 -1:无 0:电房,1:进线柜,2:补偿柜,3:滤波柜，4:馈线柜,5:配电箱,6:能源设备
     */
    @ApiModelProperty(value = "绑定的设备类型 -1:无 0:电房,1:进线柜,2:补偿柜,3:滤波柜，4:馈线柜,5:配电箱,6:能源设备")
    private Integer bindingType;

    /**
     * 在层中的水平位置
     */
    @ApiModelProperty(value = "在层中的水平位置")
    private Double storeyX;

    /**
     * 在层中的垂直位置
     */
    @ApiModelProperty(value = "在层中的垂直位置")
    private Double storeyY;

    /**
     * 在CAD图中的水平位置
     */
    @ApiModelProperty(value = "在CAD图中的水平位置")
    private Double storeyCadX;

    /**
     * 在CAD图中的垂直的位置
     */
    @ApiModelProperty(value = "在CAD图中的垂直的位置")
    private Double storeyCadY;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double jd;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double wd;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String mark;

    /**
     * 备用标记
     */
    @ApiModelProperty(value = "备用标记")
    private Integer extFlag;

    /**
     * 云视频地址
     */
    @ApiModelProperty(value = "云视频地址")
    private String videoUrl;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 硬件版本
     */
    @ApiModelProperty(value = "硬件版本")
    private String hwVersion;

    /**
     * 软件版本
     */
    @ApiModelProperty(value = "软件版本")
    private String swVersion;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private String productDate;

    /**
     * 使用寿命
     */
    @ApiModelProperty(value = "使用寿命")
    private String usefulLife;

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