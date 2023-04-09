package com.steelman.iot.platform.camera.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2020/9/26 0026 10:04
 * @Description:
 */
@Data
public class YsLiveInfoResult extends YsBasicResult {
    private List<LiveInfo> data;


    @Getter
    @Setter
    @ToString
    public class LiveInfo {
        private String deviceSerial;
        private Integer channelNo;
        private String deviceName;
        private String hls;
        private String hlsHd;
        private String rtmp;
        private String rtmpHd;
        private String status;
        private String exception;
        private String ret;
        private String desc;
    }
}
