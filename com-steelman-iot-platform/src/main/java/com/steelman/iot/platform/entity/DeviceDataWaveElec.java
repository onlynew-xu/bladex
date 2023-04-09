package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_device_data_wave_elec
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceDataWaveElec implements Serializable {
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
     * A相电压相位角,单位：0.01°
     */
    @ApiModelProperty(value = "A相电压相位角,单位：0.01°")
    private String voltPhaseAngleA;

    /**
     * B相电压相位角,单位：0.01°
     */
    @ApiModelProperty(value = "B相电压相位角,单位：0.01°")
    private String voltPhaseAngleB;

    /**
     * C相电压相位角,单位：0.01°
     */
    @ApiModelProperty(value = "C相电压相位角,单位：0.01°")
    private String voltPhaseAngleC;

    /**
     * A相电流相位角,单位：0.01°
     */
    @ApiModelProperty(value = "A相电流相位角,单位：0.01°")
    private String ampPhaseAngleA;

    /**
     * B相电流相位角,单位：0.01°
     */
    @ApiModelProperty(value = "B相电流相位角,单位：0.01°")
    private String ampPhaseAngleB;

    /**
     * C相电流相位角,单位：0.01°
     */
    @ApiModelProperty(value = "C相电流相位角,单位：0.01°")
    private String ampPhaseAngleC;

    /**
     * A相电压 单位0.01V
     */
    @ApiModelProperty(value = "A相电压 单位0.01V")
    private String voltRmsA;

    /**
     * B相电压 单位0.01V
     */
    @ApiModelProperty(value = "B相电压 单位0.01V")
    private String voltRmsB;

    /**
     * C相电压 单位0.01V
     */
    @ApiModelProperty(value = "C相电压 单位0.01V")
    private String voltRmsC;

    /**
     * A相电流，单位：0.001A
     */
    @ApiModelProperty(value = "A相电流，单位：0.001A")
    private String ampRmsA;

    /**
     * B相电流，单位：0.001A
     */
    @ApiModelProperty(value = "B相电流，单位：0.001A")
    private String ampRmsB;

    /**
     * C相电流，单位：0.001A
     */
    @ApiModelProperty(value = "C相电流，单位：0.001A")
    private String ampRmsC;

    /**
     * A相有功功率 单位：0.1var
     */
    @ApiModelProperty(value = "A相有功功率 单位：0.1var")
    private String activePowerA;

    /**
     * B相有功功率 单位：0.1var
     */
    @ApiModelProperty(value = "B相有功功率 单位：0.1var")
    private String activePowerB;

    /**
     * C相有功功率 单位：0.1var
     */
    @ApiModelProperty(value = "C相有功功率 单位：0.1var")
    private String activePowerC;

    /**
     * A相无功功率 单位：0.1var
     */
    @ApiModelProperty(value = "A相无功功率 单位：0.1var")
    private String reactivePowerA;

    /**
     * B相无功功率 单位：0.1var
     */
    @ApiModelProperty(value = "B相无功功率 单位：0.1var")
    private String reactivePowerB;

    /**
     * C相无功功率 单位：0.1var
     */
    @ApiModelProperty(value = " C相无功功率 单位：0.1var")
    private String reactivePowerC;

    /**
     * A相视在功率，单位：0.1va
     */
    @ApiModelProperty(value = "A相视在功率，单位：0.1va")
    private String apparentPowerA;

    /**
     * B相视在功率，单位：0.1va
     */
    @ApiModelProperty(value = "B相视在功率，单位：0.1va")
    private String apparentPowerB;

    /**
     * C相视在功率，单位：0.1va
     */
    @ApiModelProperty(value = "C相视在功率，单位：0.1va")
    private String apparentPowerC;

    /**
     * A相功率因数，单位：0.001
     */
    @ApiModelProperty(value = "A相功率因数，单位：0.001")
    private String powerFactorA;

    /**
     * B相功率因数，单位：0.001
     */
    @ApiModelProperty(value = "B相功率因数，单位：0.001")
    private String powerFactorB;

    /**
     * C相功率因数，单位：0.001
     */
    @ApiModelProperty(value = "C相功率因数，单位：0.001")
    private String powerFactorC;

    /**
     * 电压线频率，单位：0.001
     */
    @ApiModelProperty(value = "电压线频率，单位：0.001")
    private String lineFreq;

    /**
     * A相电流THD
     */
    @ApiModelProperty(value = "A相电流THD")
    private String thdiA;

    /**
     * B相电流THD
     */
    @ApiModelProperty(value = "B相电流THD")
    private String thdiB;

    /**
     * C相电流THD
     */
    @ApiModelProperty(value = "C相电流THD")
    private String thdiC;

    /**
     * A相电压THD
     */
    @ApiModelProperty(value = "A相电压THD")
    private String thdvA;

    /**
     * B相电压THD
     */
    @ApiModelProperty(value = "B相电压THD")
    private String thdvB;

    /**
     * C相电压THD
     */
    @ApiModelProperty(value = "C相电压THD")
    private String thdvC;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}