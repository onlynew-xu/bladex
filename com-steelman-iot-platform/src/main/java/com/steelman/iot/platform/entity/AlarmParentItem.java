package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmParentItem implements Serializable {
    private Long id;

    private String name;

    private Date createTime;

    private Date updateTime;
}
