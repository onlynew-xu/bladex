package com.steelman.iot.platform.controller;

import com.steelman.iot.platform.camera.YsCameraApi;
import com.steelman.iot.platform.camera.result.YsBasicResult;
import com.steelman.iot.platform.camera.result.YsDeviceCancelResult;
import com.steelman.iot.platform.camera.result.YsLiveInfoResult;
import com.steelman.iot.platform.camera.result.YsOpenLiveResult;
import com.steelman.iot.platform.entity.Monitor;
import com.steelman.iot.platform.entity.MonitorDevice;
import com.steelman.iot.platform.service.MonitorDeviceService;
import com.steelman.iot.platform.service.MonitorService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/5/10 0010 14:58
 * @Description:
 */
@Api(tags = "监控设备")
@RestController
@RequestMapping("/api/monitor/device")
@Slf4j
public class MonitorDeviceController {


    @Resource
    private YsCameraApi ysCameraApi;
    @Resource
    private MonitorService monitorService;
    @Resource
    private MonitorDeviceService monitorDeviceService;

    @ApiOperation("添加监控")
    @PostMapping(value = "/save", consumes = CommonUtils.MediaTypeJSON)
    public Result saveMonitor(String serialNum, String validateCode, Long projectId,String name) {

        if (StringUtils.isEmpty(serialNum) || StringUtils.isEmpty(validateCode) || Objects.isNull(projectId)||Objects.isNull(name)) {
            return Result.paramsError();
        }

        Monitor monitor = monitorService.selectBySerialNum(serialNum);
        if (!Objects.isNull(monitor)) {
            return Result.fail("该设备已被添加", null);
        }
        try {
            YsBasicResult addResult = ysCameraApi.addDevice(serialNum, validateCode, ysCameraApi.getYsToken());
            //设备添加成功 或已被添加
            if (!(addResult.getCode().equals("200") || addResult.getCode().equals("20017"))) {
                return Result.fail(addResult.getMsg(), null);
            }
            //查看设备通道信息
            List<YsDeviceCancelResult.DeviceCancelInfo> channelList = ysCameraApi.getDeviceCancelInfo(serialNum, ysCameraApi.getToken());
            StringBuilder builder = new StringBuilder();
            for (YsDeviceCancelResult.DeviceCancelInfo channel : channelList) {
                builder.append(serialNum).append(":").append(channel.getChannelNo()).append(",");
            }
            if (channelList.get(0).getIsEncrypt().equals("1")) {
                ysCameraApi.closeDevicePass(serialNum, validateCode);
            }
            YsOpenLiveResult openResult = ysCameraApi.openLive(builder.toString(), ysCameraApi.getYsToken());
            //设备开通直播或已开通成功
            if (!(openResult.getCode().equals("200") || openResult.getCode().equals("60062"))) {
                return Result.fail(openResult.getMsg(), null);
            }
            YsLiveInfoResult liveResult = ysCameraApi.getDeviceLiveInfo(builder.toString(), ysCameraApi.getToken());
            monitor = new Monitor();
            YsLiveInfoResult.LiveInfo liveInfo = liveResult.getData().get(0);
            Date now = new Date();
            monitor.setBrand("萤石云");
            monitor.setName(name);
            monitor.setProjectId(projectId);
            monitor.setSerialNum(serialNum);
            monitor.setHls(liveInfo.getHls());
            monitor.setHlsHd(liveInfo.getHlsHd());
            monitor.setRtmp(liveInfo.getRtmp());
            monitor.setRtmpHd(liveInfo.getRtmpHd());
            monitor.setValidateCode(validateCode);
            monitor.setUpdateTime(now);
            monitor.setCreateTime(now);
            monitorService.insert(monitor);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("设备添加失败", null);
        }


    }

    @ApiOperation("获取萤石云token")
    @PostMapping("/token")
    public Result getYsToken() {
        Map<String, Object> map = new HashMap<>();
        try {
            String ysToken = ysCameraApi.getToken();
            if (StringUtils.isEmpty(ysToken)) {
                return Result.fail("获取token失败", null);
            }
            map.put("ysToken", ysToken);
            return Result.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取token失败", null);
        }
    }

    @ApiOperation("删除监控")
    @PostMapping("/remove")
    public Result removeMonitor(@ApiParam(name = "id", value = "监控Id", required = true) @RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return Result.paramsError();
        }

        Monitor monitor = monitorService.findById(id);
        if (Objects.isNull(monitor)) {
            return Result.fail("设备不存在", null);
        }
        try {
            YsBasicResult result = ysCameraApi.unbindDevice(monitor.getSerialNum(), ysCameraApi.getToken());
            if (!result.getCode().equals("200")) {
                return Result.fail(result.getMsg(), null);
            }
            monitorService.delete(monitor.getId());
            //删除监控下的设备
            monitorDeviceService.deleteByMonitorId(monitor.getId());
        } catch (Exception e) {
            return Result.fail("设备解绑失败", null);
        }
        return Result.success();
    }


    @ApiOperation("设备绑定")
    @PostMapping("/device/bind/")
    public Result monitorBindDevice(@ApiParam(name = "deviceId", value = "设备Id", required = true) @RequestParam("deviceId") Long deviceId,
                                    @ApiParam(name = "monitorId", value = "监控Id", required = true) @RequestParam("monitorId") Long monitorId) {

        if (Objects.isNull(deviceId) || Objects.isNull(monitorId)) {
            return Result.paramsError();
        }
        MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(deviceId);
        if (!Objects.isNull(monitorDevice)) {
            return Result.fail("该设备已绑定监控", null);
        }
        Monitor monitor = monitorService.findById(monitorId);
        if(Objects.isNull(monitor)){
            return Result.fail("监控不存在", null);
        }
        monitorDevice = new MonitorDevice();
        monitorDevice.setDeviceId(deviceId);
        Date now = new Date();
        monitorDevice.setUpdateTime(now);
        monitorDevice.setCreateTime(now);
        monitorDevice.setMonitorId(monitorId);
        monitorDevice.setHlsHd(monitor.getHlsHd());
        monitorDevice.setDeviceId(deviceId);
        monitorDeviceService.insert(monitorDevice);
        return Result.success();
    }

    @ApiOperation("设备解绑监控")
    @PostMapping(value = "/device/unbind",produces = CommonUtils.MediaTypeJSON)
    public Result monitorUnbindDevice(@ApiParam(name = "deviceId", value = "设备Id", required = true) @RequestParam("deviceId") Long deviceId,
                                      @ApiParam(name = "monitorId", value = "监控Id", required = true) @RequestParam("monitorId") Long monitorId){
        if(Objects.isNull(monitorId) || Objects.isNull(deviceId)){
            return Result.paramsError();
        }
        monitorDeviceService.deleteDeviceMonitor(monitorId,deviceId);
        return Result.success();
    }

    @ApiOperation("监控列表")
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public Result getMonitorList(@RequestBody Map<String, Object> paramMap) {
        Object projectId = paramMap.get("projectId");
        if (Objects.isNull(projectId)) {
            return Result.paramsError();
        }
        List<Monitor> monitors = monitorService.selectByProjectId(Long.valueOf(projectId.toString()));
        return Result.success(monitors);
    }

    @ApiOperation("设备监控信息")
    @PostMapping("/device")
    public Result getDeviceMonitor(@RequestBody Map<String, Object> paramMap) {
        Object deviceId = paramMap.get("deviceId");
        if (Objects.isNull(deviceId)) {
            return Result.paramsError();
        }
        MonitorDevice monitorDevice = monitorDeviceService.getDeviceMonitorHls(Long.valueOf(deviceId.toString()));
        log.info("monitor ===>{}", monitorDevice);
        Monitor monitor = monitorService.findById(monitorDevice.getMonitorId());
        return Result.success(monitor);
    }

}
