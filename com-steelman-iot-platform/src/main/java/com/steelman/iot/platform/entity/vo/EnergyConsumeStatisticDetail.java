package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumeStatisticDetail {
    private String month;
    private List<DayConsumeData> dayConsume=new ArrayList<>();
}
