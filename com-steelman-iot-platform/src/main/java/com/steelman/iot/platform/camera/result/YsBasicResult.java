package com.steelman.iot.platform.camera.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lsj
 * @DATE 2020/9/26 0026 9:52
 * @Description:
 */
@Data
public class YsBasicResult implements Serializable {
    private String code;
    private String msg;
}
