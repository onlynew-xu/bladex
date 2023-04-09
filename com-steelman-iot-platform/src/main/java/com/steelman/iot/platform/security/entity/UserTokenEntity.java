package com.steelman.iot.platform.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenEntity implements Serializable {
    private Long userId;
    private String token;
}
