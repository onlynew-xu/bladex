package com.steelman.iot.platform.camera.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2020/9/26 0026 9:54
 * @Description:
 */
@Data
public class YsOpenLiveResult  extends YsBasicResult implements Serializable {
    private List<OpenLiveInfo> data;

    @Getter
    @Setter
    @ToString
    public class OpenLiveInfo{
        private String deviceSerial;
        private String channelNo;
        private String ret;
        private String desc;
    }
}
