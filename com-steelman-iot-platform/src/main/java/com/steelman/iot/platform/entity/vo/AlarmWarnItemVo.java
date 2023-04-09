package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/13 0013 15:46
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmWarnItemVo {
    private Long id;
    private Long deviceId;
    private String powerName;
    private String deviceName;
    private Date createTime;
    private String alarmItemName;
    private Integer handleFlag;
    private Integer status;
    private Integer bindingType;
    private Long loopId;
    private String loopName;



}
