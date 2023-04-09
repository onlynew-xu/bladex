package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyStatisticYearDetail {
    private String year;
    private List<MonthConsumeData> monthConsumeDataList=new ArrayList<>();
}
