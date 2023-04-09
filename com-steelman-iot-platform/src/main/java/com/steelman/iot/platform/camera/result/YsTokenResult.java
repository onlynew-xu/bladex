package com.steelman.iot.platform.camera.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lsj
 * @DATE 2020/9/26 0026 9:48
 * @Description:
 */
@Data
public class YsTokenResult  extends YsBasicResult implements Serializable {
    private YsToken data;

    @Data
    public class YsToken {
        private String accessToken;
        private Long expireTime;
    }
}
