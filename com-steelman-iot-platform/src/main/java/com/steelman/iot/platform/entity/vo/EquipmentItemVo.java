package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author lsj
 * @DATE 2021/4/9 0009 17:26
 * @Description:
 */
@Data
public class EquipmentItemVo {
    private String url;
    private Long id;
    private Date createTime;
}
