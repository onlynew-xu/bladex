package com.steelman.iot.platform.camera;

import com.alibaba.fastjson.JSONObject;
import com.steelman.iot.platform.camera.result.*;
import com.steelman.iot.platform.utils.HttpWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author lsj
 * @DATE 2021/4/21 0021 14:25
 * @Description:
 */
@Component
@Slf4j
public class YsCameraApi {

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${ys.appKey}")
    private String appKey;
    @Value("${ys.appSecret}")
    private String appSecret;
    @Value("${ys.tokenUrl}")
    private String tokenUrl; //获取tokenUrl
    @Value("${ys.addDeviceUrl}")
    private String addDeviceUrl; //添加设备url
    @Value("${ys.deviceCancelUrl}")
    private String deviceCancelUrl; //获取设备通道信息URL
    @Value("${ys.openLiveUrl}")
    private String openLiveUrl; //开启直播url
    @Value("${ys.unbindDeviceUrl}")
    private String unbindDeviceUrl;
    @Value("${ys.getLiveUrl}")
    private String getLiveUrl;

    public String getToken() throws Exception {
        Object obj = redisTemplate.opsForValue().get("ys_camera_token");
        if (Objects.isNull(obj)) {
            return getYsToken();
        } else {
            return obj.toString();
        }
    }

    public String getYsToken() throws Exception {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("appKey", appKey);
        paramsMap.put("appSecret", appSecret);

        String result = HttpWebUtils.postFormUrl("https://open.ys7.com/api/lapp/token/get",paramsMap);
        System.out.println(result);
        YsTokenResult ysTokenResult = JSONObject.parseObject(result, YsTokenResult.class);
        long time = ysTokenResult.getData().getExpireTime() - System.currentTimeMillis() - 600000;
        redisTemplate.opsForValue().set("ys_camera_token", ysTokenResult.getData().getAccessToken(), time, TimeUnit.MILLISECONDS);
        return ysTokenResult.getData().getAccessToken();
    }


    public YsBasicResult addDevice(String serialNum, String validateCode, String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("deviceSerial", serialNum.toUpperCase());
        map.put("validateCode", validateCode.toUpperCase());
        String result = HttpWebUtils.postFormUrl(addDeviceUrl, map);
        YsBasicResult entity = JSONObject.parseObject(result, YsBasicResult.class);
        return entity;
    }

    /**
     * 开启设备直播
     *
     * @param source
     * @return
     */
    public YsOpenLiveResult openLive(String source, String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("source", source);
        String result = HttpWebUtils.postFormUrl(openLiveUrl, map);
        YsOpenLiveResult openLiveResponse = JSONObject.parseObject(result, YsOpenLiveResult.class);
        return openLiveResponse;
    }

    /**
     * 删除设备
     *
     * @param deviceSerial
     * @return
     */
    public YsBasicResult unbindDevice(String deviceSerial, String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("deviceSerial", deviceSerial.toUpperCase());
        map.put("accessToken", token);
        String result = HttpWebUtils.postFormUrl(unbindDeviceUrl, map);
        YsBasicResult entity = JSONObject.parseObject(result, YsBasicResult.class);
        return entity;
    }

    /**
     * 设备直播信息
     *
     * @param source
     * @return
     */
    public YsLiveInfoResult getDeviceLiveInfo(String source, String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("source", source);
        String result = HttpWebUtils.postFormUrl(getLiveUrl, map);
        YsLiveInfoResult liveInfoResult = JSONObject.parseObject(result, YsLiveInfoResult.class);
        return liveInfoResult;
    }

    /**
     * 获取设备通道信息
     *
     * @param deviceSerial
     */
    public List<YsDeviceCancelResult.DeviceCancelInfo> getDeviceCancelInfo(String deviceSerial, String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);
        map.put("deviceSerial", deviceSerial.toUpperCase());
        String resultStr = HttpWebUtils.postFormUrl(deviceCancelUrl, map);
        YsDeviceCancelResult result = JSONObject.parseObject(resultStr, YsDeviceCancelResult.class);
        if (!result.getCode().equals("200")) {
            return new ArrayList<>();
        } else {
            List<YsDeviceCancelResult.DeviceCancelInfo> data = result.getData();
            return data;
        }
    }


    public YsBasicResult closeDevicePass(String serialNum, String validateCode) throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", getToken());
        map.put("validateCode", validateCode.toUpperCase());
        map.put("deviceSerial", serialNum.toUpperCase());
        String resultStr = HttpWebUtils.postFormUrl("https://open.ys7.com/api/lapp/device/encrypt/off", map);
        YsBasicResult entity = JSONObject.parseObject(resultStr, YsBasicResult.class);
        log.info("entity ===>{}", entity.toString());
        return entity;
    }
}
