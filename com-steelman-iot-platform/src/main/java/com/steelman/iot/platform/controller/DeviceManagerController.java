package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.device.MT300CDeviceRemoteService;
import com.steelman.iot.platform.device.MT300DeviceRemoteService;
import com.steelman.iot.platform.device.SafeDeviceRemoteService;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.DeviceType;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.DianQiDeviceCenterInfo;
import com.steelman.iot.platform.entity.vo.XiaoFangDeviceCenterInfo;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.*;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/device/manager")
public class DeviceManagerController extends BaseController {
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceSystemService deviceSystemService;
    @Resource
    private DeviceParamsSafeElecService deviceParamsSafeElecService;
    @Resource
    private SafeDeviceRemoteService safeDeviceRemoteService;
    @Resource
    private MT300CDeviceRemoteService mt300CDeviceRemoteService;
    @Resource
    private MT300DeviceRemoteService mt300DeviceRemoteService;
    @Resource
    private DeviceParamSmokeService deviceParamSmokeService;
    @Resource
    private DeviceParamsWaterService deviceParamsWaterService;
    @Resource
    private DeviceParamsDoorService deviceParamsDoorService;
    @Resource
    private DeviceParamsSmartElecService deviceParamsSmartElecService;
    @Resource
    private DeviceTypeAlarmService deviceTypeAlarmService;
    @Resource
    private AlarmItemService alarmItemService;
    @Resource
    private AlarmConfigService alarmConfigService;
    @Resource
    private DeviceParamsWaveElecService deviceParamsWaveElecService;
    @Resource
    private DeviceParamsCompensateElecService deviceParamsCompensateElecService;
    @Resource
    private ParamsTemperaturehumidityService paramsTemperaturehumidityService;
    @Resource
    private MonitorService monitorService;
    @Resource
    private MonitorDeviceService monitorDeviceService;
    @Resource
    private DeviceTypeService deviceTypeService;

    /**
     * 获取所有设备类型
     * @return
     */
    @Log("获取设备类型列表信息")
    @RequestMapping(value = "/deviceTypeList",produces = CommonUtils.MediaTypeJSON)
    public String deviceTypeList(){
        Result<List<DeviceType>> result=new Result<>();
        result.setCode(0);
        try {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            if(CollectionUtils.isEmpty(deviceTypeList)){
                result=Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(deviceTypeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/deviceTypeList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceType>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 根据设备类型获取设备列表
     * @param paramData
     * @return
     */
    @Log("根据设备类型获取设备列表")
    @PostMapping(value = "/deviceList",produces = CommonUtils.MediaTypeJSON)
    public String deviceList(@RequestBody Map<String,Object> paramData){
        Result<List<Device>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceTypeObj = paramData.get("deviceTypeId");
            if(projectObj==null||deviceTypeObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long deviceTypeId=Long.parseLong(deviceTypeObj.toString());
                List<Device> deviceList=deviceService.selectByDeviceTypeId(projectId,deviceTypeId);
                if(CollectionUtils.isEmpty(deviceList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(deviceList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/deviceList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<Device>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除设备
     * @param paramData
     * @return
     */
    @Log("删除设备")
    @PostMapping(value = "/remove/device",produces = CommonUtils.MediaTypeJSON)
    public String removeDevice(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceIdObj  = paramData.get("deviceId");
            if(projectObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Boolean flag=deviceService.removeDevice(projectId,deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/remove/device"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 设备详细信息
     * @param paramMap
     * @return
     */
    @Log("设备详情信息")
    @PostMapping(value = "/device/detail",produces = CommonUtils.MediaTypeJSON)
    public String deviceInfo(@RequestBody Map<String,Object> paramMap){
       Result<Object> result=new Result<>();
       result.setCode(0);
        try {
            Object deviceIdObj = paramMap.get("deviceId");
            Object deviceTypeIdObj = paramMap.get("deviceTypeId");
            Object projectIdObj = paramMap.get("projectId");
            if(deviceIdObj==null||projectIdObj==null||deviceTypeIdObj==null){
                Result.paramError(result);
            }else{
                Long deviceTypeId=Long.parseLong(deviceTypeIdObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Device device=deviceService.findById(deviceId);
                //电气火灾
                if(deviceTypeId.equals(1L)||deviceTypeId.equals(3L)){
                    DianQiDeviceCenterInfo dianQiDeviceCenterInfo=deviceService.getSafeDeviceCenterInfo(projectId,deviceId,1000L);
                     if(dianQiDeviceCenterInfo!=null){
                         dianQiDeviceCenterInfo.setDeviceName(device.getName());
                         result.setCode(1);
                         result.setData(dianQiDeviceCenterInfo);
                     }
                //消防电源
                }else if(deviceTypeId.equals(2L)||deviceTypeId.equals(4L)){
                    XiaoFangDeviceCenterInfo xiaoFangDeviceCenterInfo=deviceService.getXiaoFangCenterInfo(projectId,deviceId,1000L);
                    if(xiaoFangDeviceCenterInfo!=null){
                        xiaoFangDeviceCenterInfo.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(xiaoFangDeviceCenterInfo);
                    }
                //625
                }else if(deviceTypeId.equals(5L)){
                    SafeMT300CDeviceCenter safeMT300CDeviceCenter=deviceService.safeMT300CDeviceInfo(projectId,deviceId);
                    if(safeMT300CDeviceCenter!=null){
                        safeMT300CDeviceCenter.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeMT300CDeviceCenter);
                    }
                //1128
                }else if(deviceTypeId.equals(6l)){
                    SafeMT300DeviceCenter safeMT300DeviceCenter=deviceService.safeMT300DeviceInfo(projectId,deviceId);
                    if(safeMT300DeviceCenter!=null){
                        safeMT300DeviceCenter.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeMT300DeviceCenter);
                    }
                //烟感设备
                }else if(deviceTypeId.equals(7l)){
                    SafeSmokeDeviceInfo safeSmokeDeviceInfo=deviceService.smokeInfo(projectId,deviceId);
                    if(safeSmokeDeviceInfo!=null){
                        safeSmokeDeviceInfo.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeSmokeDeviceInfo);
                    }
                //末端试水
                }else if(deviceTypeId.equals(8l)){
                    SafeWaterDeviceInfo safeWaterDeviceInfo= deviceService.waterSafeInfo(deviceId,projectId);
                    if(safeWaterDeviceInfo!=null){
                        safeWaterDeviceInfo.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeWaterDeviceInfo);
                    }
                //防火门
                }else if(deviceTypeId.equals(9l)){
                    SafeDoorDeviceInfo safeDoorDeviceInfo=deviceService.doorInfo(projectId,deviceId);
                     if(safeDoorDeviceInfo!=null){
                         safeDoorDeviceInfo.setDeviceName(device.getName());
                         result.setCode(1);
                         result.setData(safeDoorDeviceInfo);
                     }
                //滤波控制器
                }else if(deviceTypeId.equals(10l)){
                    SafeWaveDeviceCenter safeWaveDeviceCenter=deviceService.safeWaveDeviceInfo(projectId,deviceId);
                    if(safeWaveDeviceCenter!=null){
                        safeWaveDeviceCenter.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeWaveDeviceCenter);
                    }
                //补偿控制器
                }else if(deviceTypeId.equals(11l)){
                    SafeCompensateDeviceCenter safeCompensateDeviceCenter=deviceService.safeCompensateDeviceInfo(projectId,deviceId);
                    if(safeCompensateDeviceCenter!=null){
                        safeCompensateDeviceCenter.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeCompensateDeviceCenter);
                    }
                //温湿度
                }else if(deviceTypeId.equals(12l)){
                    SafeTempatureHumidity safeTempatureHumidity=deviceService.tempatureHumidityInfo(projectId,deviceId);
                    if(safeTempatureHumidity!=null){
                        safeTempatureHumidity.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeTempatureHumidity);
                    }
                //1129
                }else if(deviceTypeId.equals(13l)) {
                    SafeMT300SDeviceCenter safeMT300SDeviceCenter = deviceService.safeMT300SDeviceInfo(projectId, deviceId);
                    if(safeMT300SDeviceCenter!=null){
                        safeMT300SDeviceCenter.setDeviceName(device.getName());
                        result.setCode(1);
                        result.setData(safeMT300SDeviceCenter);
                    }
                }else{
                    result.setMsg("设备类型信息错误");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/device/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Object>>(){}.getType();
       return JsonUtils.toJson(result,type);
    }

    /**
     * 修改设备参数页面
     * @param paramMap
     * @return
     */
    @Log("修改设备参数返回数据")
    @PostMapping(value = "/device/detail/info")
    public String deviceParams(@RequestBody Map<String,Object> paramMap){
        Result<Object> result=new Result<>();
        try {
            Object deviceTypeObj = paramMap.get("deviceTypeId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(deviceTypeObj==null||deviceIdObj==null){
                Result.paramError(result);
            }else{
                Long deviceTypeId = Long.parseLong(deviceTypeObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                if(deviceTypeId.equals(1L)||deviceTypeId.equals(2L)||deviceTypeId.equals(3L)||deviceTypeId.equals(4L)){
                    SafeDeviceVariableParam safeDeviceVariableParam=deviceService.getSafeDeviceVariableParam(deviceId);
                    if(safeDeviceVariableParam!=null){
                        result.setData(safeDeviceVariableParam);
                        result.setCode(1);
                    }else{
                       result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(5L)||deviceTypeId.equals(6L)){
                    SmartDeviceVariableParam smartDeviceVariableParam=deviceService.getSmartDeviceVariableParam(deviceId);
                    if(smartDeviceVariableParam!=null){
                        result.setData(smartDeviceVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(7L)){
                    SmokeDeviceVariableParam smokeDeviceVariableParam=deviceService.getSmokeDeviceVariableParam(deviceId);
                    if(smokeDeviceVariableParam!=null){
                        result.setData(smokeDeviceVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(8L)){
                    WaterDeviceVariableParam waterDeviceVariableParam=deviceService.getWaterDeviceVariableParam(deviceId);
                    if(waterDeviceVariableParam!=null){
                        result.setData(waterDeviceVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(9L)){
                    DoorDeviceVariableParams doorDeviceVariableParams=deviceService.getDoorWaterVariableParam(deviceId);
                    if(doorDeviceVariableParams!=null){
                        result.setData(doorDeviceVariableParams);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(10l)){
                    WaveVariableParam waveVariableParam=deviceService.getWaveVariableParam(deviceId);
                    if(waveVariableParam!=null){
                        result.setData(waveVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(11l)){
                    CompensateVariableParam compensateVariableParam=deviceService.getCompensateVariableParam(deviceId);
                    if(compensateVariableParam!=null){
                        result.setData(compensateVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(12l)){
                    TempatureVariableParam tempatureVariableParam=deviceService.getTempatureVariableParam(deviceId);
                    if(tempatureVariableParam!=null){
                        result.setData(tempatureVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }else if(deviceTypeId.equals(13l)){
                    SuperDeviceVariableParam superDeviceVariableParam=  deviceService.getSuperDeviceVariableParam(deviceId);
                    if(superDeviceVariableParam!=null){
                        result.setData(superDeviceVariableParam);
                        result.setCode(1);
                    }else{
                        result= Result.empty(result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/device/detail/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Object>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 更新设备参数
     * @param paramMap
     * @return
     */
    @Log("更新设备参数")
    @PostMapping(value = "/updateDevice",produces = CommonUtils.MediaTypeJSON)
    public String updateDevice(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceObj = paramMap.get("deviceId");
            Object projectObj = paramMap.get("projectId");
            Object deviceTypeObj = paramMap.get("deviceTypeId");
            if(deviceObj==null||projectObj==null||deviceTypeObj==null){
                result = Result.paramError(result);
            }else{
               Boolean flag=deviceService.updateDeviceParam(paramMap);
               if(flag){
                   result.setCode(1);
                   result.setData(1);
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/updateDevice"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<String>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加设备
     * @param paramMap
     * @return
     */
    @Log("添加设备")
    @PostMapping(value = "/saveDevice",produces = CommonUtils.MediaTypeJSON)
    public String saveDevice(@RequestBody Map<String,Object> paramMap) {

        Result<String> result = new Result<>();
        result.setCode(0);
        try {
            String serialNum = paramMap.get("serialNum").toString();
            Device device = deviceService.selectBySerialNum(serialNum);
            if(device!=null){
                logger.info("设备已经存在");
                result.setCode(0);
                result.setData("设备sn已使用,请更改新的设备");
                Type type=new TypeToken<Result<String>>(){}.getType();
                return JsonUtils.toJson(result,type);
            }
            result = deviceService.saveDevice(paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/device/manager/saveDevice"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<String>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
