package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataSmartElecFloatDto {
    //数据Id
    private Long id;

    //终端设备Id
    private Long deviceId;

    //终端设备SN
    private String serialNum;


    /**
     * A相电压 单位0.01V
     */
    private Float voltRmsA;

    /**
     * B相电压 单位0.01V
     */
    private Float voltRmsB;

    /**
     * C相电压 单位0.01V
     */
    private Float voltRmsC;

    /**
     * A相电流 单位：0.001A
     */
    private Float ampRmsA;

    /**
     * B相电流 单位：0.001A
     */
    private Float ampRmsB;

    /**
     * C相电流 单位：0.001A
     */
    private Float ampRmsC;

    /**
     * 漏电电流 单位mA
     */
    private Float leaked;

    /**
     * 温度1 单位0.1℃
     */
    private Float temp1;

    /**
     * 温度2 单位0.1℃
     */
    private Float temp2;

    /**
     * 温度3 单位0.1℃
     */
    private Float temp3;

    /**
     * 温度4 单位0.1℃
     */
    private Float temp4;

    /**
     * A相有功功率 单位：0.1w
     */
    private Float activePowerA;

    /**
     * B相有功功率 单位：0.1w
     */
    private Float activePowerB;

    /**
     * C相有功功率 单位：0.1w
     */
    private Float activePowerC;

    /**
     * A相无功功率 单位：0.1var
     */
    private Float reactivePowerA;

    /**
     * B相无功功率 单位：0.1var
     */
    private Float reactivePowerB;

    /**
     * C相无功功率 单位：0.1var
     */
    private Float reactivePowerC;

    /**
     * A相视在功率 单位：0.1va
     */
    private Float apparentPowerA;

    /**
     * B相视在功率 单位：0.1va
     */
    private Float apparentPowerB;

    /**
     * C相视在功率 单位：0.1va
     */
    private Float apparentPowerC;

    /**
     * A相功率因数 单位：0.001
     */
    private Integer powerFactorA;

    /**
     * B相功率因数 单位：0.001
     */
    private Integer  powerFactorB;

    /**
     * C相功率因数 单位：0.001
     */
    private Integer powerFactorC;

    /**
     * 电压线频率 单位：0.001
     */
    private Float lineFreq;

    /**
     * A相电流THD 单位0.1%
     */
    private Float thdiA;

    /**
     * B相电流THD 单位0.1%
     */
    private Float thdiB;

    /**
     * C相电流THD 单位0.1%
     */
    private Float thdiC;

    /**
     * A相电压THD 单位0.1%
     */
    private Float thdvA;

    /**
     * B相电压THD 单位0.1%
     */
    private Float thdvB;

    /**
     * C相电压THD 单位0.1%
     */
    private Float thdvC;


}
