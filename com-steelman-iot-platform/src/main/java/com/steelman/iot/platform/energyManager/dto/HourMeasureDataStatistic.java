package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourMeasureDataStatistic {
    //时
    private String hour;
    //日期
    private String dateDetail;
    //尖时电度
    private Object daySpike;
    //峰时电度
    private Object dayPeak;
    //平峰电度
    private Object dayNormal;
    //谷时电度
    private Object dayValley;
    //总电度
    private Object dayTotal;

}
