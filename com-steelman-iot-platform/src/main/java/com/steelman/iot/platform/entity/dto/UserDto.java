package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String mobile;
    private Integer status;
    private Integer admin;
    private Integer sex;
    private Date birth;
    private String picture;
    private String pictureUrl;
    private String address;
    private String hobby;
    private String province;
    private String city;
    private String district;
    private String dept;
    private Date createTime;
    private Date updateTime;
}
