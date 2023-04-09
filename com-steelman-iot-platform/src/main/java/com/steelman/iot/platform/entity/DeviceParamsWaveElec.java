package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_params_wave_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceParamsWaveElec implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String serialNum;

    /**
     * 数据上报间隔，单位为秒，3-3600
     */
    @ApiModelProperty(value = "数据上报间隔，单位为秒，3-3600")
    private String reportInterval;

    /**
     * 变压器容量
     */
    @ApiModelProperty(value = "变压器容量")
    private String transCapacity;

    /**
     * 变压器负荷
     */
    @ApiModelProperty(value = "变压器负荷")
    private String transLoad;

    /**
     * RS485地址 0-247
     */
    @ApiModelProperty(value = "RS485地址 0-247")
    private String modbusAddress;

    /**
     * RS485地址波特率 2400，9600，115200
     */
    @ApiModelProperty(value = "RS485地址波特率 2400，9600，115200")
    private String modbusBaudrate;

    /**
     * 高峰时间段上报时间
     */
    @ApiModelProperty(value = "高峰时间段上报时间")
    private String peakReportTime;

    /**
     * 平峰时间段上报时间
     */
    @ApiModelProperty(value = "平峰时间段上报时间")
    private String normalReportTime;

    /**
     * 低谷时间段上报时间
     */
    @ApiModelProperty(value = "低谷时间段上报时间")
    private String valleyReportTime;

    /**
     * A相CT转换比例 200:1：200
     */
    @ApiModelProperty(value = "A相CT转换比例 200:1：200")
    private String ctRatioA;

    /**
     * B相CT转换比例 200:1：200
     */
    @ApiModelProperty(value = "B相CT转换比例 200:1：200")
    private String ctRatioB;

    /**
     * C相CT转换比例 200:1：200
     */
    @ApiModelProperty(value = "C相CT转换比例 200:1：200")
    private String ctRatioC;

    /**
     * 漏电CT转换比例200:1：200
     */
    @ApiModelProperty(value = "漏电CT转换比例200:1：200")
    private String ctRatioD;

    /**
     * 功率因数a 单位：0.001
     */
    @ApiModelProperty(value = "功率因数a 单位：0.001")
    private String powerFactorA;

    /**
     * 功率因数b 单位：0.001
     */
    @ApiModelProperty(value = "功率因数b 单位：0.001")
    private String powerFactorB;

    /**
     * 功率因数c 单位：0.001
     */
    @ApiModelProperty(value = "功率因数c 单位：0.001")
    private String powerFactorC;

    /**
     * 过压a 单位：V
     */
    @ApiModelProperty(value = "过压a 单位：V")
    private String overVoltageA;

    /**
     * 过压b 单位：V
     */
    @ApiModelProperty(value = "过压b 单位：V")
    private String overVoltageB;

    /**
     * 过压c 单位：V
     */
    @ApiModelProperty(value = "过压c 单位：V")
    private String overVoltageC;

    /**
     * 欠压a 单位：V
     */
    @ApiModelProperty(value = "欠压a 单位：V")
    private String underVoltageA;

    /**
     * 欠压b 单位：V
     */
    @ApiModelProperty(value = "欠压b 单位：V")
    private String underVoltageB;

    /**
     * 欠压c 单位：V
     */
    @ApiModelProperty(value = "欠压c 单位：V")
    private String underVoltageC;

    /**
     * 缺相a 单位：V
     */
    @ApiModelProperty(value = "缺相a 单位：V")
    private String lackPhaseA;

    /**
     * 缺相b 单位：V
     */
    @ApiModelProperty(value = "缺相b 单位：V")
    private String lackPhaseB;

    /**
     * 缺相c 单位：V
     */
    @ApiModelProperty(value = "缺相c 单位：V")
    private String lackPhaseC;

    /**
     * 过流a 单位：A
     */
    @ApiModelProperty(value = "过流a 单位：A")
    private String overCurrentA;

    /**
     * 过流b 单位：A 
     */
    @ApiModelProperty(value = "过流b 单位：A ")
    private String overCurrentB;

    /**
     * 过流c 单位：A
     */
    @ApiModelProperty(value = "过流c 单位：A")
    private String overCurrentC;

    /**
     * 电流畸变率a 单位：%
     */
    @ApiModelProperty(value = "电流畸变率a 单位：%")
    private String currentThdA;

    /**
     * 电流畸变率b 单位：%
     */
    @ApiModelProperty(value = "电流畸变率b 单位：%")
    private String currentThdB;

    /**
     * 电流畸变率c 单位：%
     */
    @ApiModelProperty(value = "电流畸变率c 单位：%")
    private String currentThdC;

    /**
     * 电压畸变率a 单位：%
     */
    @ApiModelProperty(value = "电压畸变率a 单位：%")
    private String voltageThdA;

    /**
     * 电压畸变率b 单位：%
     */
    @ApiModelProperty(value = "电压畸变率b 单位：%")
    private String voltageThdB;

    /**
     * 电压畸变率c 单位：%
     */
    @ApiModelProperty(value = "电压畸变率c 单位：%")
    private String voltageThdC;

    /**
     * //DIDO使能,4 BYTES Byte0：1 DIDO通道4使能 Byte1：1 DIDO通道3使能 Byte2：0 DIDO通道2禁用 Byte3：1 DIDO通道1使能
     */
    @ApiModelProperty(value = "//DIDO使能,4 BYTES Byte0：1 DIDO通道4使能 Byte1：1 DIDO通道3使能 Byte2：0 DIDO通道2禁用 Byte3：1 DIDO通道1使能")
    private String didoSet;

    /**
     * 系统开关 7 BYTES  0：禁止 1：使能 ;0服务器1使能开关 ,Byte1服务器2使能开关,2 MODBUS使能开关,3定时上报使能开关,4峰平谷上报使能开关,5峰平谷电价使能开关,6峰平谷时间使能开关
     */
    @ApiModelProperty(value = "系统开关 7 BYTES  0：禁止 1：使能 ;0服务器1使能开关 ,Byte1服务器2使能开关,2 MODBUS使能开关,3定时上报使能开关,4峰平谷上报使能开关,5峰平谷电价使能开关,6峰平谷时间使能开关")
    private String systemSw;

    /**
     * 报警开关: 13 BYTES 0：禁止 1：使能 0 变压器负荷报警使能开关,1 A相功率因数报警使能开关,2 B相功率因数报警使能开关,3 C相功率因数报警使能开关,4 A相过压、欠压、缺相报警使能开关,5 B相过压、欠压、缺相报警使能开关,6 C相过压、欠压、缺相报警使能开关,7 A相过流报警使能开关,8 B相过流报警使能开关,9 C相过流报警使能开关,10 A相谐波含量报警使能开关,11 B相谐波含量报警使能开关,C相谐波含量报警使能开关
     */
    @ApiModelProperty(value = "报警开关: 13 BYTES 0：禁止 1：使能 0 变压器负荷报警使能开关,1 A相功率因数报警使能开关,2 B相功率因数报警使能开关,3 C相功率因数报警使能开关,4 A相过压、欠压、缺相报警使能开关,5 B相过压、欠压、缺相报警使能开关,6 C相过压、欠压、缺相报警使能开关,7 A相过流报警使能开关,8 B相过流报警使能开关,9 C相过流报警使能开关,10 A相谐波含量报警使能开关,11 B相谐波含量报警使能开关,C相谐波含量报警使能开关")
    private String alarmSw;

    /**
     * 高峰时间段
     */
    @ApiModelProperty(value = "高峰时间段")
    private String peak;

    /**
     * 平峰时间段
     */
    @ApiModelProperty(value = "平峰时间段")
    private String normal;

    /**
     * 低谷时间段
     */
    @ApiModelProperty(value = "低谷时间段")
    private String valley;

    /**
     * 高峰时间段电价 单位：0.01元
     */
    @ApiModelProperty(value = "高峰时间段电价 单位：0.01元")
    private String peakPrice;

    /**
     * 平峰时间段电价 单位：0.01元
     */
    @ApiModelProperty(value = "平峰时间段电价 单位：0.01元")
    private String normalPrice;

    /**
     * 低谷时间段电价 单位：0.01元
     */
    @ApiModelProperty(value = "低谷时间段电价 单位：0.01元")
    private String valleyPrice;

    /**
     * 100：成功 -1：正在进行数据解析,请等待。 -2：Json 格式错误 -3：设置失败 -100: 其他
     */
    @ApiModelProperty(value = "100：成功 -1：正在进行数据解析,请等待。 -2：Json 格式错误 -3：设置失败 -100: 其他")
    private String returnCode;

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