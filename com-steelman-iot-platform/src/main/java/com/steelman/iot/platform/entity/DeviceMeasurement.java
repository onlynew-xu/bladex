package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_device_measurement
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class DeviceMeasurement implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    /**
     * sn码
     */
    @ApiModelProperty(value = "sn码")
    private String serialNum;

    /**
     * 项目Id
     */
    private Long projectId;



    /**
     * 当日高峰期电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日高峰期电度数 单位0.001kwh")
    private String daySpike;


    /**
     * 当日高峰期电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日高峰期电度数 单位0.001kwh")
    private String dayPeak;

    /**
     * 当日平峰期电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日平峰期电度数 单位0.001kwh")
    private String dayNormal;

    /**
     * 当日低谷期电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日低谷期电度数 单位0.001kwh")
    private String dayValley;

    /**
     * 当日总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日总电度数 单位0.001kwh")
    private String dayTotal;

    /**
     * 当日高峰电费 单位元
     */
    @ApiModelProperty(value = "当日尖时电费 单位元")
    private String daySpikePrice;


    /**
     * 当日高峰电费 单位元
     */
    @ApiModelProperty(value = "当日高峰电费 单位元")
    private String dayPeakPrice;

    /**
     * 当日平峰电费 单位元
     */
    @ApiModelProperty(value = "当日平峰电费 单位元")
    private String dayNormalPrice;

    /**
     * 当日低谷期电费 单位元
     */
    @ApiModelProperty(value = "当日低谷期电费 单位元")
    private String dayValleyPrice;

    /**
     * 当日总电费 单位元
     */
    @ApiModelProperty(value = "当日总电费 单位元")
    private String dayTotalPrice;

    /**
     * 月尖时电度 单位0.001kwh
     */
    @ApiModelProperty(value = "月尖时电度 单位0.001kwh")
    private String monthSpike;

    /**
     * 月高峰期电度 单位0.001kwh
     */
    @ApiModelProperty(value = "月高峰期电度 单位0.001kwh")
    private String monthPeak;

    /**
     * 月平峰期电度 单位0.001kwh
     */
    @ApiModelProperty(value = "月平峰期电度 单位0.001kwh")
    private String monthNormal;

    /**
     * 月低谷期电度 单位0.001kwh
     */
    @ApiModelProperty(value = "月低谷期电度 单位0.001kwh")
    private String monthValley;

    /**
     * 月总电度 单位0.001kwh
     */
    @ApiModelProperty(value = "月总电度 单位0.001kwh")
    private String monthTotal;



    /**
     * 尖时总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "尖时总电度数 单位0.001kwh")
    private String totalSpike;

    /**
     * 高峰期总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "高峰期总电度数 单位0.001kwh")
    private String totalPeak;

    /**
     * 平峰期总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "平峰期总电度数 单位0.001kwh")
    private String totalNormal;

    /**
     * 低谷期总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "低谷期总电度数 单位0.001kwh")
    private String totalValley;

    /**
     * 总电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "总电度数 单位0.001kwh")
    private String totalTotal;

    /**
     * 当日高峰期无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "当日尖期无功电度数 单位0.001Kvar")
    private String reactiveDaySpike;


    /**
     * 当日高峰期无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "当日高峰期无功电度数 单位0.001Kvar")
    private String reactiveDayPeak;

    /**
     * 当日平峰期无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "当日平峰期无功电度数 单位0.001Kvar")
    private String reactiveDayNormal;

    /**
     * 当日低谷期无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "当日低谷期无功电度数 单位0.001Kvar")
    private String reactiveDayValley;

    /**
     * 当日总无功电度数 单位0.001kwh
     */
    @ApiModelProperty(value = "当日总无功电度数 单位0.001kwh")
    private String reactiveDayTotal;

    /**
     * 月尖时无功电度 单位0.001Kvar
     */
    @ApiModelProperty(value = "月尖时无功电度 单位0.001Kvar")
    private String reactiveMonthSpike;

    /**
     * 月高峰期无功电度 单位0.001Kvar
     */
    @ApiModelProperty(value = "月高峰期无功电度 单位0.001Kvar")
    private String reactiveMonthPeak;

    /**
     * 月平峰期无功电度 单位0.001Kvar
     */
    @ApiModelProperty(value = "月平峰期无功电度 单位0.001Kvar")
    private String reactiveMonthNormal;

    /**
     * 月低谷期无功电度 单位0.001Kvar
     */
    @ApiModelProperty(value = "月低谷期无功电度 单位0.001Kvar")
    private String reactiveMonthValley;

    /**
     * 月总无功电度 单位0.001Kvar
     */
    @ApiModelProperty(value = "月总无功电度 单位0.001Kvar")
    private String reactiveMonthTotal;


    /**
     * 尖时总无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "尖时总无功电度数 单位0.001Kvar")
    private String reactiveTotalSpike;

    /**
     * 高峰期总无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "高峰期总无功电度数 单位0.001Kvar")
    private String reactiveTotalPeak;

    /**
     * 平峰期总无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "平峰期总无功电度数 单位0.001Kvar")
    private String reactiveTotalNormal;

    /**
     * 低谷期总无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "低谷期总无功电度数 单位0.001Kvar")
    private String reactiveTotalValley;

    /**
     * 总无功电度数 单位0.001Kvar
     */
    @ApiModelProperty(value = "总无功电度数 单位0.001Kvar")
    private String reactiveTotalTotal;

    /**
     * 年
     */
    @ApiModelProperty(value = "年")
    private String year;

    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;

    /**
     * 日
     */
    @ApiModelProperty(value = "日")
    private String day;
    /**
     * 年月日
     */
    private String yearMonthDay;

    /**
     * 主动查询标志
     */
    private  Integer manualFlag;

    /**
     * 当月最后一天 0:不是  1:是
     */
    @ApiModelProperty(value = "当月最后一天 0:不是  1:是")
    private Integer monthLastDay;

    /**
     * 当天最后一分钟(主动查询获取的数据 0:不是 1:是)
     */
    @ApiModelProperty(value = "当天最后一分钟(主动查询获取的数据 0:不是 1:是)")
    private Integer dayLastMin;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}