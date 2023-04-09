package com.steelman.iot.platform.camera.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2020/9/26 0026 15:01
 * @Description:
 */
@Data
public class YsDeviceCancelResult extends YsBasicResult implements Serializable {
    private List<DeviceCancelInfo> data;

    @Getter
    @Setter
    @ToString
    public class DeviceCancelInfo {
        private String deviceSerial; //序列
        private String ipcSerial; //IPC序列号
        private Integer channelNo; //通道号
        private String deviceName; //设备名
        private String channelName; //通道名
        private int status; //在线状态：0-不在线，1-在线,-1设备未上报
        private String picUrl; //图片地址（大图），若在萤石客户端设置封面则返回封面图片，未设置则返回默认图片
        private String isEncrypt; //是否加密，0：不加密，1：加密
        private String videoLevel; //视频质量：0-流畅，1-均衡，2-高清，3-超清
        private String relatedIpc; //当前通道是否关联IPC：true-是，false-否。设备未上报或者未关联都是false
    }
}
