package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.Power;
import com.steelman.iot.platform.entity.PowerDevice;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 10:26
 * @Description:
 */
@RestController
@RequestMapping("/api/power/manager")
public class PowerManagerController extends BaseController {

    @Resource
    private PowerService powerService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private PowerDeviceService powerDeviceService;

    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerGeneratorService powerGeneratorService;

    /**
     * 电房列表信息
     * @param paramMap
     * @return
     */
    @Log("电房列表信息")
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String getPowerList(@RequestBody Map<String,Object> paramMap) {
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                List<Power> powerList = powerService.selectByProjectId(projectId);
                if(CollectionUtils.isEmpty(powerList)){
                   result= Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Power power : powerList) {
                        dataList.add(new EntityDto(power.getId(),power.getName(),power.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改电房名称
     * @param paramMap
     * @return
     */
    @Log("修改电房名称")
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String updatePower(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            Object powerNameObj = paramMap.get("powerName");
            Object projectIdObj = paramMap.get("projectId");
            if(powerIdObj==null||powerNameObj==null||projectIdObj==null){
               result= Result.paramError(result);
            }else{
                String powerName=powerNameObj.toString();
                Long powerId=Long.parseLong(powerIdObj.toString());
                Power powerInfo = powerService.getPowerInfo(powerId);
                if(powerInfo.getName().equals(powerName)){
                    result.setMsg("电房名称信息未改变,请修改后重试");
                }else{
                    Long projectId=Long.parseLong(projectIdObj.toString());
                    Power power=powerService.selectByNameAndProjectId(powerName,projectId);
                    if(power!=null){
                        result.setMsg("电房名称信息已存在,请修改后重试");
                    }else{
                        Power powerNew=new Power();
                        powerNew.setId(powerId);
                        powerNew.setName(powerName);
                        powerNew.setUpdateTime(new Date());
                        powerService.update(powerNew);
                        result.setCode(1);
                        result.setData(1);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 添加电房
     * @param paramMap
     * @return
     */
    @Log("添加电房")
    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String savePower(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object powerNameObj = paramMap.get("powerName");
            Object projectIdObj = paramMap.get("projectId");
            if(powerNameObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                String powerName = powerNameObj.toString();
                Long projectId = Long.parseLong(projectIdObj.toString());
                Power powerOld = powerService.selectByNameAndProjectId(powerName, projectId);
                if(powerOld!=null){
                    result.setMsg("电房名称已存在，请修改后重试");
                }else{
                    Power power = new Power();
                    power.setName(powerName);
                    power.setProjectId(projectId);
                    Date date = new Date();
                    power.setCreateTime(date);
                    power.setUpdateTime(date);
                    powerService.insert(power);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除电房
     * @param paramMap
     * @return
     */
    @Log("删除电房")
    @PostMapping(value = "/remove", produces = CommonUtils.MediaTypeJSON)
    public String removePower(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
                Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                List<PowerTransformer> powerTransformers = powerTransformerService.selectByPowerId(powerId);
                //无变压器可删除
                if(CollectionUtils.isEmpty(powerTransformers)){
                    //删除电房
                    powerService.deleteById(powerId);
                    //重置绑定设备状态
                    List<PowerDevice> deviceList = powerDeviceService.selectByPowerId(powerId);
                    Device device = new Device();
                    for (PowerDevice powerDevice : deviceList) {
                        device.setBindingStatus(0);
                        device.setBindingType(-1);
                        device.setId(powerDevice.getId());
                        deviceService.update(device);
                    }
                    //删除绑定关系
                    powerDeviceService.deleteByPowerId(powerId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败，电房中还有变压器相关数据");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 电房设备列表
     * @param paramMap
     * @return
     */
    @Log("获取电房绑定的设备列表信息")
    @PostMapping(value = "/device/list", produces = CommonUtils.MediaTypeJSON)
    public String getPowerDeviceList(@RequestBody Map<String,Object> paramMap) {
        Result<List<PowerDeviceInfo>> result= null;
        try {
            result = new Result<>();
            result.setCode(0);
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList = powerDeviceService.getPowerDeviceList(powerId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                    result=Result.paramError(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 电房添加设备
     * @param paramMap
     * @return
     */
    @Log("电房添加绑定设备")
    @PostMapping(value = "/device/save", produces = CommonUtils.MediaTypeJSON)
    public String savePowerDevice(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(powerIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
               Long powerId=  Long.parseLong(powerIdObj.toString());
               Long deviceId=  Long.parseLong(deviceIdObj.toString());
               Device device = deviceService.findById(deviceId);
                if (device.getBindingStatus() == 1) {
                    result.setMsg("设备已绑定，请修改后重试");
                }else{
                    //完善设备绑定
                    Date date = new Date();
                    Device deviceNew=new Device();
                    deviceNew.setBindingType(0); //电房设备
                    deviceNew.setBindingStatus(1); //被绑定
                    deviceNew.setUpdateTime(date);
                    deviceNew.setId(deviceId);
                    deviceService.update(deviceNew);
                    //添加电房设备绑定
                    Power power = powerService.getPowerInfo(powerId);
                    PowerDevice powerDevice = new PowerDevice();
                    powerDevice.setDeviceId(deviceId);
                    powerDevice.setPowerId(powerId);
                    powerDevice.setDeviceId(device.getId());
                    powerDevice.setProjectId(power.getProjectId());
                    powerDevice.setName(device.getName());
                    powerDevice.setCreateTime(date);
                    powerDevice.setUpdateTime(date);
                    powerDeviceService.insert(powerDevice);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/device/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 电房解绑设备
     * @param paraMap
     * @return
     */
    @Log("电房解绑设备")
    @PostMapping(value = "/device/remove", produces = CommonUtils.MediaTypeJSON)
    public String deletePowerDevice(@RequestBody Map<String,Object> paraMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object powerDeviceIdObj = paraMap.get("powerDeviceId");
            Object deviceIdObj = paraMap.get("deviceId");
            if(powerDeviceIdObj==null||deviceIdObj==null){
               result= Result.paramError(result);
            }else{
                Long powerDeviceId = Long.parseLong(powerDeviceIdObj.toString());
                Long deviceId = Long.parseLong(deviceIdObj.toString());
                //删除电房绑定信息
                powerDeviceService.delete(powerDeviceId);
               //更新设备绑定状态
                Device device=new Device();
                device.setId(deviceId);
                device.setBindingStatus(0);
                device.setBindingType(-1);
                device.setUpdateTime(new Date());
                deviceService.update(device);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/device/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}


