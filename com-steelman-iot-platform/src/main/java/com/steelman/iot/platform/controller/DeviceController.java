package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.device.MT300DeviceRemoteService;
import com.steelman.iot.platform.device.MT300SDeviceRemoteService;
import com.steelman.iot.platform.device.SafeDeviceRemoteService;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.DeviceTask;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.DeviceStatisticsInfo;
import com.steelman.iot.platform.entity.vo.DianQiDeviceCenterInfo;
import com.steelman.iot.platform.entity.vo.SafeDeviceCenter;
import com.steelman.iot.platform.entity.vo.XiaoFangDeviceCenterInfo;
import com.steelman.iot.platform.service.DeviceService;
import com.steelman.iot.platform.service.DeviceTaskService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-12-03
 */
@RestController
@RequestMapping("/api/device")
public class DeviceController extends BaseController {
    @Resource
    private DeviceService deviceService;
    @Resource
    private SafeDeviceRemoteService safeDeviceRemoteService;
    @Resource
    private MT300DeviceRemoteService mt300DeviceRemoteService;
    @Resource
    private MT300SDeviceRemoteService mt300SDeviceRemoteService;
    @Resource
    private DeviceTaskService deviceTaskService;

    /**
     * 安全与预警 设备实时在线数量统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/deviceStatistics",produces = CommonUtils.MediaTypeJSON)
    public String deviceStatistics(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceStatisticsInfo>> result=new Result<>();
        Object projectObj = paramMap.get("projectId");
        result.setCode(0);
        try {
            if(projectObj==null){
                 result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceStatisticsInfo> deviceStatistic = deviceService.getDeviceStatistic(projectId,1000L);
                if(CollectionUtils.isEmpty(deviceStatistic)){
                    result=  Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(deviceStatistic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/deviceStatistics"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceStatisticsInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警 设备中心接口
     * @param paramDate
     * @return
     */
    @PostMapping(value = "/safe/device/center",produces = CommonUtils.MediaTypeJSON)
    public String safeDeviceCenter(@RequestBody Map<String,Object> paramDate){
        Result<List<SafeDeviceCenter>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramDate.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<SafeDeviceCenter> dataMap=deviceService.safeDeviceCenter(projectId,1000L);
                if(CollectionUtils.isEmpty(dataMap)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/center"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<SafeDeviceCenter>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 安全预警 电气火灾设备详情页
     * @param param
     * @return
     */
    @PostMapping(value = "/safe/device/dianQi",produces = CommonUtils.MediaTypeJSON)
    public String getDianQiInfo(@RequestBody Map<String,Object> param){
        Result<DianQiDeviceCenterInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            Object deviceObj = param.get("deviceId");
            if(projectObj==null||deviceObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long deviceId=Long.parseLong(deviceObj.toString());
                DianQiDeviceCenterInfo dianQiDeviceCenterInfo =deviceService.getSafeDeviceCenterInfo(projectId,deviceId,1000L);
                if(dianQiDeviceCenterInfo ==null){
                   result= Result.empty(result);
                }else{
                    result.setData(dianQiDeviceCenterInfo);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/dianQi"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<DianQiDeviceCenterInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  消防电源设备详情页
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/xiaoFang",produces = CommonUtils.MediaTypeJSON)
    public String getXiaoFangInfo(@RequestBody Map<String,Object> paramData){
        Result<XiaoFangDeviceCenterInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long deviceId=Long.parseLong(deviceObj.toString());
                XiaoFangDeviceCenterInfo xiaoFangDeviceCenterInfo=deviceService.getXiaoFangCenterInfo(projectId,deviceId,1000l);
                if(xiaoFangDeviceCenterInfo==null){
                    result=Result.empty(result);
                }else{
                    result.setData(xiaoFangDeviceCenterInfo);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/xiaoFang"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<XiaoFangDeviceCenterInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 安全预警  烟感设备
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/smoke" ,produces = CommonUtils.MediaTypeJSON)
    public String getSmokeInfo(@RequestBody Map<String,Object> paramData){
        Result<SafeSmokeDeviceInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeSmokeDeviceInfo safeSmokeDeviceInfo=deviceService.smokeInfo(projectId,deviceId);
                if(safeSmokeDeviceInfo==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeSmokeDeviceInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/smoke"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeSmokeDeviceInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 安全预警 末端试水
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/water",produces = CommonUtils.MediaTypeJSON)
    public String getWaterInfo(@RequestBody Map<String,Object> paramData){
        Result<SafeWaterDeviceInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            Object projectObj = paramData.get("projectId");
            if(projectObj==null||deviceObj==null){
                result=Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(deviceObj.toString());
                SafeWaterDeviceInfo safeWaterDeviceInfo= deviceService.waterSafeInfo(deviceId,projectId);
                if(safeWaterDeviceInfo==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeWaterDeviceInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/water"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeWaterDeviceInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警 防火门
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/door" ,produces = CommonUtils.MediaTypeJSON)
    public String getDoorInfo(@RequestBody Map<String,Object> paramData){
        Result<SafeDoorDeviceInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeDoorDeviceInfo safeDoorDeviceInfo=deviceService.doorInfo(projectId,deviceId);
                if(safeDoorDeviceInfo==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeDoorDeviceInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/door"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeDoorDeviceInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  mt300
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/mt300" ,produces = CommonUtils.MediaTypeJSON)
    public String getSafeMT300Info(@RequestBody Map<String,Object> paramData) {
        Result<SafeMT300DeviceCenter> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeMT300DeviceCenter safeMT300DeviceCenter=deviceService.safeMT300DeviceInfo(projectId,deviceId);
                if(safeMT300DeviceCenter==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeMT300DeviceCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/mt300"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeTempatureHumidity>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 安全预警  mt300c
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/mt300c" ,produces = CommonUtils.MediaTypeJSON)
    public String getSafeMT300CInfo(@RequestBody Map<String,Object> paramData) {
        Result<SafeMT300CDeviceCenter> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeMT300CDeviceCenter safeMT300CDeviceCenter=deviceService.safeMT300CDeviceInfo(projectId,deviceId);
                if(safeMT300CDeviceCenter==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeMT300CDeviceCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/mt300c"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeMT300CDeviceCenter>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警MT300S
      * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/mt300s" ,produces = CommonUtils.MediaTypeJSON)
    public String getSafeMT300SInfo(@RequestBody Map<String,Object> paramData) {
        Result<SafeMT300SDeviceCenter> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeMT300SDeviceCenter safeMT300SDeviceCenter=deviceService.safeMT300SDeviceInfo(projectId,deviceId);
                if(safeMT300SDeviceCenter==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeMT300SDeviceCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/mt300s"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeMT300CDeviceCenter>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  补偿控制器
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/compensate" ,produces = CommonUtils.MediaTypeJSON)
    public String getSafeCompensateInfo(@RequestBody Map<String,Object> paramData) {
        Result<SafeCompensateDeviceCenter> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeCompensateDeviceCenter safeCompensateDeviceCenter=deviceService.safeCompensateDeviceInfo(projectId,deviceId);
                if(safeCompensateDeviceCenter==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeCompensateDeviceCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/compensate"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeCompensateDeviceCenter>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  滤波控制器
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/wave" ,produces = CommonUtils.MediaTypeJSON)
    public String getSafeWaveInfo(@RequestBody Map<String,Object> paramData) {
        Result<SafeWaveDeviceCenter> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeWaveDeviceCenter safeCompensateDeviceCenter=deviceService.safeWaveDeviceInfo(projectId,deviceId);
                if(safeCompensateDeviceCenter==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeCompensateDeviceCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/wave"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeWaveDeviceCenter>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



    /**
     * 安全预警  温湿度
     * @param paramData
     * @return
     */
    @RequestMapping(value ="/safe/device/temperatureHumidity" ,produces = CommonUtils.MediaTypeJSON)
    public String getTempatureHumidityInfo(@RequestBody Map<String,Object> paramData){
        Result<SafeTempatureHumidity> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result =Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceObj.toString());
                Long projectId=Long.parseLong(projectObj.toString());
                SafeTempatureHumidity safeTempatureHumidity=deviceService.tempatureHumidityInfo(projectId,deviceId);
                if(safeTempatureHumidity==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(safeTempatureHumidity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/temperatureHumidity"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeTempatureHumidity>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  设备消音
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/erasure",produces = CommonUtils.MediaTypeJSON)
    public String deviceErasure(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            Object deviceTypeObj = paramData.get("deviceTypeId");
            if(deviceObj==null||deviceTypeObj==null){
                result=Result.paramError(result);
            }else{
                Long deviceId = Long.parseLong(deviceObj.toString());
                Long deviceTypeId = Long.parseLong(deviceTypeObj.toString());
                Device device = deviceService.findById(deviceId);
                if(device!=null){
                    if(deviceTypeId.equals(1L)||deviceTypeId.equals(2L)||deviceTypeId.equals(3L)||deviceTypeId.equals(4L)){
                        Boolean erasure = safeDeviceRemoteService.erasure(device.getSerialNum());
                        if(erasure){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6l)){
                        Boolean erasure = mt300DeviceRemoteService.setErasure(device.getSerialNum());
                        if(erasure){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13l)){
                        Boolean erasure = mt300SDeviceRemoteService.setErasure(device.getSerialNum());
                        if(erasure){
                            result.setCode(1);
                            result.setData(1);
                        }
                        //其他类型直接返回成功
                    }else{
                        result.setCode(1);
                        result.setData(1);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"设备消音"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警   设备自检
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/selfInspection",produces = CommonUtils.MediaTypeJSON)
    public String deviceSelfInspection(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            Object deviceTypeObj = paramData.get("deviceTypeId");
            if(deviceObj==null||deviceTypeObj==null){
                result=Result.paramError(result);
            }else{
                Long deviceId = Long.parseLong(deviceObj.toString());
                Long deviceTypeId = Long.parseLong(deviceTypeObj.toString());
                Device device = deviceService.findById(deviceId);
                if(device!=null){
                    if(deviceTypeId.equals(1L)||deviceTypeId.equals(2L)||deviceTypeId.equals(3L)||deviceTypeId.equals(4L)){
                        Boolean selfInspection = safeDeviceRemoteService.selfInspection(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6l)){
                        Boolean selfInspection = mt300DeviceRemoteService.selfInspection(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13l)) {
                        Boolean selfInspection = mt300SDeviceRemoteService.selfInspection(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }

                    }else{
                        result.setCode(1);
                        result.setData(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"设备自检"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警   设备复位
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/restoration",produces = CommonUtils.MediaTypeJSON)
    public String deviceRestoration(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            Object deviceTypeObj = paramData.get("deviceTypeId");
            if(deviceObj==null||deviceTypeObj==null){
                result=Result.paramError(result);
            }else{
                Long deviceId = Long.parseLong(deviceObj.toString());
                Long deviceTypeId = Long.parseLong(deviceTypeObj.toString());
                Device device = deviceService.findById(deviceId);
                if(device!=null){
                    if(deviceTypeId.equals(1l)||deviceTypeId.equals(2l)||deviceTypeId.equals(3l)||deviceTypeId.equals(4l)){
                        Boolean restoration = safeDeviceRemoteService.restoration(device.getSerialNum());
                        if(restoration){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6l)){
                        Boolean restoration = mt300DeviceRemoteService.setRestoration(device.getSerialNum());
                        if(restoration){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13l)) {
                        Boolean restoration = mt300SDeviceRemoteService.setRestoration(device.getSerialNum());
                        if(restoration){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else{
                        result.setCode(1);
                        result.setData(1);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"设备复位"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



    /**
     * 安全预警  移除设备
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/delete",produces = CommonUtils.MediaTypeJSON)
    public String safeDeleteDevice(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectObj.toString());
                Long deviceId = Long.parseLong(deviceObj.toString());
                Boolean flag=deviceService.removeByDeviceSafeSystem(projectId,deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/delete"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 设备维保详情页
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/maintenance/detail",produces = CommonUtils.MediaTypeJSON)
    public String deviceMaintenanceDetail(@RequestBody Map<String,Object> paramData){
        Result<DeviceMaintenanceDetail> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            if(deviceObj==null){
                result= Result.paramError(result);
            }else{
                Long deviceId = Long.parseLong(deviceObj.toString());
                DeviceMaintenanceDetail deviceMaintenanceDetail = deviceService.getDeviceMaintenanceDetail(deviceId);
                if(deviceMaintenanceDetail!=null){
                    result.setCode(1);
                    result.setData(deviceMaintenanceDetail);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/maintenance/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<DeviceMaintenanceDetail>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 设备维保上报
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/device/maintenance",produces = CommonUtils.MediaTypeJSON)
    public String dianQiMaintenance(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramData.get("deviceId");
            Object userObj = paramData.get("userId");
            Object detailObj = paramData.get("detail");
            Object projectObj = paramData.get("projectId");
            if(deviceObj==null||userObj==null||detailObj==null||projectObj==null){
               result= Result.paramError(result);
            }else{
                Long deviceId = Long.parseLong(deviceObj.toString());
                Long userId = Long.parseLong(userObj.toString());
                String detail=detailObj.toString();
                Long projectId=Long.parseLong(projectObj.toString());
                Device device = deviceService.findById(deviceId);
                if(device==null){
                    result.setMsg("设备不存在");
                }else{
                    Date date=new Date();
                    DeviceTask deviceTask=new DeviceTask();
                    deviceTask.setSystemId(1000L);
                    deviceTask.setSerialNum(device.getSerialNum());
                    deviceTask.setProjectId(projectId);
                    deviceTask.setDetail(detail);
                    deviceTask.setCreateTime(date);
                    deviceTask.setDeviceStatus(device.getStatus());
                    deviceTask.setDeviceId(device.getId());
                    deviceTask.setStatus(0);
                    deviceTask.setUpdateTime(date);
                    deviceTask.setUserId(userId);
                    deviceTask.setWorkerId(null);
                    deviceTaskService.insert(deviceTask);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/safe/device/maintenance"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 设备添加到系统
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/add/system",produces = CommonUtils.MediaTypeJSON)
    public String addDeviceSystem(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object serialNumObj = paramMap.get("serialNum");
            Object systemIdObj = paramMap.get("systemId");
            if(serialNumObj==null||systemIdObj==null){
               result=Result.paramError(result);
            }else{
                String serialNum=serialNumObj.toString();
                Long systemId=Long.parseLong(systemIdObj.toString());
                Boolean flag=deviceService.addDeviceSystem(serialNum,systemId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/add/system"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "/system/list",produces = CommonUtils.MediaTypeJSON)
    public String systemDeviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object deviceTypeIdObj = paramMap.get("deviceTypeId");
            Object systemIdObj = paramMap.get("systemId");
            if(projectIdObj==null||deviceTypeIdObj==null||systemIdObj==null){
                result= Result.paramError(result);
            }else{
                Long deviceTypeId=Long.parseLong(deviceTypeIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long systemId=Long.parseLong(systemIdObj.toString());
                List<EntityDto> deviceSystemInfoList=deviceService.getSystemDeviceList(projectId,deviceTypeId,systemId);
                if(CollectionUtils.isEmpty(deviceSystemInfoList)){
                    result= Result.empty(result);
                }else{
                    result.setData(deviceSystemInfoList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/system/list"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

}
