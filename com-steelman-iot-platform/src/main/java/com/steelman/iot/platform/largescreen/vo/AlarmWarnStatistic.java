package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("告警数据统计")
public class AlarmWarnStatistic {

    @ApiModelProperty(value = "告警总数")
    private Integer totalCount;

    @ApiModelProperty(value = "今日告警数")
    private Integer dayCount;

    @ApiModelProperty(value = "昨日告警数")
    private Integer yesterdayCount;

    @ApiModelProperty(value = "周告警数")
    private Integer weekCount;

    @ApiModelProperty(value = "月告警数")
    private Integer monthCount;

    @ApiModelProperty(value = "告警已处理数")
    private Integer handlerCount;

    @ApiModelProperty(value = "告警未处理数")
    private Integer inHandlerCount;

    @ApiModelProperty(value = "告警处理率")
    private String percent;



}
