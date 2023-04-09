package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyHourDifferData  {
    private String yearMonthDay;
    private Long  hourTotal;
    private String dateTimeBegin;
    private String dateTimeEnd;

}
