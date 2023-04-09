package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.PowerBoxLoopDeviceService;
import com.steelman.iot.platform.service.PowerBoxLoopService;
import com.steelman.iot.platform.service.PowerBoxService;
import com.steelman.iot.platform.service.PowerComponentsService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping("/api/power/box/manager")
public class PowerBoxManagerController extends BaseController {

    @Resource
    private PowerBoxService powerBoxService;
    @Resource
    private PowerBoxLoopService powerBoxLoopService;
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerBoxLoopDeviceService powerBoxLoopDeviceService;
    /**
     * 配电箱列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "list",produces = CommonUtils.MediaTypeJSON)
    public String boxList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerBox>> result=new Result<>();
        result.setCode(0);
        try {
            Object parentTypeObj = paramMap.get("parentType");
            Object loopIdObj = paramMap.get("loopId");
            if(parentTypeObj==null||loopIdObj==null){
                result=Result.paramError(result);
            }else{
                int parentType = Integer.parseInt(parentTypeObj.toString());
                Long loopId=Long.parseLong(loopIdObj.toString());
                List<PowerBox> boxListByLoopId = powerBoxService.getBoxListByLoopId(loopId, parentType);
                if(CollectionUtils.isEmpty(boxListByLoopId)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(boxListByLoopId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerBox>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加配电箱
     * @param paramMap
     * @return
     */
    @PostMapping(value = "save",produces = CommonUtils.MediaTypeJSON)
    public String boxSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerBox powerBox=new PowerBox();
            String errorMsg=checkPowerBoxParam(paramMap,powerBox);
            if(StringUtils.isNotBlank(errorMsg)){
                result= Result.paramError(result);
                result.setMsg(errorMsg);
            }else {
                PowerBox powerBoxOld= powerBoxService.getByBoxNameAndTransformerId(powerBox.getName(),powerBox.getPowerId());
                if(powerBoxOld!=null){
                    result.setMsg("配电箱名称已存在");
                }else{
                    powerBox.setCreateTime(new Date());
                    powerBox.setUpdateTime(powerBox.getCreateTime());
                    powerBoxService.insert(powerBox);
                    //插入配电箱回路
                    PowerBoxLoop powerBoxLoop=new PowerBoxLoop();
                    powerBoxLoop.setName("总回路");
                    powerBoxLoop.setBoxId(powerBox.getId());
                    powerBoxLoop.setPowerId(powerBox.getPowerId());
                    powerBoxLoop.setProjectId(powerBox.getProjectId());
                    powerBoxLoop.setTotalFlag(1);
                    powerBoxLoop.setCreateTime(powerBox.getCreateTime());
                    powerBoxLoop.setUpdateTime(powerBoxLoop.getCreateTime());
                    powerBoxLoopService.insert(powerBoxLoop);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/save"));
            result=Result.exceptionRe(result);
        }

        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改配电箱名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "update", produces = CommonUtils.MediaTypeJSON)
    public String boxUpdate(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            Object boxNameObj = paramMap.get("boxName");
            if (Objects.isNull(boxIdObj) || Objects.isNull(boxNameObj)) {
              result= Result.paramError(result);
            }else{
                Long boxId = Long.parseLong(boxIdObj.toString());
                String boxName = boxNameObj.toString();
                PowerBox boxInfo = powerBoxService.getBoxInfo(boxId);
                if(boxInfo.getName().equals(boxName)){
                    result.setMsg("配电箱名称为改变,请修改后重试");
                }else{
                    PowerBox powerBox = powerBoxService.getByBoxNameAndTransformerId(boxName, boxInfo.getTransformerId());
                    if(powerBox!=null){
                        result.setMsg("配电箱名称已存在,请修改后重试");
                    }else{
                        PowerBox box = new PowerBox();
                        box.setId(boxId);
                        box.setName(boxName);
                        box.setUpdateTime(new Date());
                        powerBoxService.update(box);
                        result.setCode(1);
                        result.setData(1);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除配电箱
     * @param paramMap
     * @return
     */
    @Log("删除配电箱")
    @PostMapping(value = "remove", produces = CommonUtils.MediaTypeJSON)
    public String boxRemove(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            if (Objects.isNull(boxIdObj)) {
                result= Result.paramError(result);
            }else{
                Long boxId = Long.parseLong(boxIdObj.toString());
                List<PowerBoxLoop> boxLoop = powerBoxLoopService.getBoxLoop(boxId);
                List<PowerComponents> componentsList = powerComponentsService.getComponentsList(boxId, 0, 5);
                List<PowerBox> finalPowerBox=new ArrayList<>();
                List<PowerDeviceInfo> finalBoxDeviceList=new ArrayList<>();
                if(!CollectionUtils.isEmpty(boxLoop)){
                    for (PowerBoxLoop powerBoxLoop : boxLoop) {
                        Long loopId = powerBoxLoop.getId();
                        List<PowerBox> boxListByLoopId = powerBoxService.getBoxListByLoopId(loopId, 1);
                        if(!CollectionUtils.isEmpty(boxListByLoopId)){
                            finalPowerBox.addAll(boxListByLoopId);
                        }
                        List<PowerDeviceInfo> deviceList = powerBoxLoopDeviceService.getDeviceList(loopId);
                        if(!CollectionUtils.isEmpty(deviceList)){
                            finalBoxDeviceList.addAll(deviceList);
                        }
                    }
                }
                if(CollectionUtils.isEmpty(componentsList) && CollectionUtils.isEmpty(finalPowerBox) &&  CollectionUtils.isEmpty(finalBoxDeviceList)){
                    powerBoxService.removePowerBox(boxId);
                    powerBoxLoopService.removeByBoxId(boxId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败，有回路和配线箱");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱元器件列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/list",produces = CommonUtils.MediaTypeJSON)
    public String componentsList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerComponentsDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            Object projectIdObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if(boxIdObj==null||projectIdObj==null||typeObj==null){
                result= Result.paramError(result);
            }else{
                Long  boxId=Long.parseLong(boxIdObj.toString());
                Long  projectId=Long.parseLong(projectIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                if(type.equals(0)){
                    type=null;
                }
                List<PowerComponentsDto> list = powerComponentsService.selectComponentsByPowerType(5,boxId,type,projectId);
                if(CollectionUtils.isEmpty(list)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/components/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerComponentsDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱添加元器件
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/save",produces = CommonUtils.MediaTypeJSON)
    public String componentsSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerComponents powerComponents=new PowerComponents();
            String msg=checkComponentsParam(paramMap,powerComponents);
            if(org.springframework.util.StringUtils.isEmpty(msg)){
                powerComponents.setPowerDeviceType(5);
                powerComponents.setCreateTime(new Date());
                powerComponents.setUpdateTime(powerComponents.getCreateTime());
                powerComponentsService.insert(powerComponents);
                result.setCode(1);
                result.setData(1);
            }else{
                result.setMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/components/update/info",produces = CommonUtils.MediaTypeJSON)
    public String componentsUpdateInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerComponentsDto> result=new Result<>();
        result.setCode(0);
        try {
            Object componentsIdObj = paramMap.get("componentsId");
            Object projectIdObj = paramMap.get("projectId");
            if(componentsIdObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long componentsId=Long.parseLong(componentsIdObj.toString());
                PowerComponentsDto powerComponentsDto=powerComponentsService.getComponentsInfo(projectId,componentsId);
                if(powerComponentsDto!=null){
                    result.setCode(1);
                    result.setData(powerComponentsDto);
                }else{
                    result=  Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerComponentsDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 配电箱修改元器件属性
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/update",produces = CommonUtils.MediaTypeJSON)
    public String componentsUpdate(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object componentsId = paramMap.get("componentsId");
            if(componentsId==null){
                result=Result.paramError(result);
            }else{
                Boolean flag=powerComponentsService.updateComponents(paramMap);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 配电箱删除元器件
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/remove",produces = CommonUtils.MediaTypeJSON)
    public String componentsRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object componentsIdObj = paramMap.get("componentsId");
            if(componentsIdObj==null){
                result=Result.paramError(result);
            }else{
                Long componentsId=Long.parseLong(componentsIdObj.toString());
                powerComponentsService.delete(componentsId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱回路列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/list",produces = CommonUtils.MediaTypeJSON)
    public String loopList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerBoxLoop>> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            if(boxIdObj==null){
                result=Result.paramError(result);
            }else{
                Long boxId=Long.parseLong(boxIdObj.toString());
                List<PowerBoxLoop> powerBoxLoopList = powerBoxLoopService.getBoxLoop(boxId);
                if(CollectionUtils.isEmpty(powerBoxLoopList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerBoxLoopList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerBoxLoop>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 配电箱添加回路
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/save",produces = CommonUtils.MediaTypeJSON)
    public String loopSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            Object loopNameObj = paramMap.get("loopName");
            if(boxIdObj==null||loopNameObj==null){
                result=Result.paramError(result);
            }else{
                Long boxId=Long.parseLong(boxIdObj.toString());
                String loopName=loopNameObj.toString();
                PowerBoxLoop powerBoxLoopOld = powerBoxLoopService.getBoxLoopInfoByBoxAndName(boxId,loopName);
                PowerBox boxInfo = powerBoxService.getBoxInfo(boxId);
                Integer loopType = boxInfo.getLoopType();
                if(loopType.equals(0)){
                    result.setMsg("单回路馈线柜无法添加回路");
                    Type type=new TypeToken<Result<Integer>>(){}.getType();
                    return JsonUtils.toJson(result,type);
                }
                if(powerBoxLoopOld==null){
                    PowerBox powerBox = powerBoxService.getBoxInfo(boxId);
                    PowerBoxLoop powerBoxLoop=new PowerBoxLoop();
                    powerBoxLoop.setTotalFlag(0);
                    powerBoxLoop.setName(loopName);
                    powerBoxLoop.setProjectId(powerBox.getProjectId());
                    powerBoxLoop.setPowerId(powerBox.getPowerId());
                    powerBoxLoop.setBoxId(boxId);
                    powerBoxLoop.setCreateTime(new Date());
                    powerBoxLoop.setUpdateTime(powerBox.getCreateTime());
                    powerBoxLoopService.insert(powerBoxLoop);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("名称已存在,请修改后重试");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱修改回路名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/update",produces = CommonUtils.MediaTypeJSON)
    public String loopUpdate(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("boxLoopId");
            Object nameObj = paramMap.get("boxLoopName");
            if(idObj==null||nameObj==null){
                result=Result.paramError(result);
            }else{
                Long boxId=Long.parseLong(idObj.toString());
                String boxName=nameObj.toString();
                PowerBoxLoop powerBoxLoop = powerBoxLoopService.getBoxLoopInfo(boxId);
                if(powerBoxLoop.getName().equals(boxName)){
                    result.setMsg("名称为改变,请修改后重试");
                }else{
                    PowerBoxLoop powerBoxLoopOld = powerBoxLoopService.getBoxLoopInfoByBoxAndName(powerBoxLoop.getBoxId(),boxName);
                    if(powerBoxLoopOld==null){
                        PowerBoxLoop powerBoxLoopNew=new PowerBoxLoop();
                        powerBoxLoopNew.setName(boxName);
                        powerBoxLoopNew.setId(boxId);
                        powerBoxLoopNew.setUpdateTime(new Date());
                        powerBoxLoopService.update(powerBoxLoopNew);
                        result.setCode(1);
                        result.setData(1);
                    }else{
                        result.setMsg("名称已存在,请修改后重试");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱删除回路
     * @param paramMap
     * @return
     */
    @Log("配电箱删除回路")
    @PostMapping(value = "/loop/remove",produces = CommonUtils.MediaTypeJSON)
    public String loopRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("boxLoopId");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long boxLoopId=Long.parseLong(idObj.toString());
                List<PowerBox> powerBoxList=powerBoxService.getBoxListByLoopId(boxLoopId,1);
                List<PowerDeviceInfo> deviceList = powerBoxLoopDeviceService.getDeviceList(boxLoopId);
                if(CollectionUtils.isEmpty(powerBoxList)&& CollectionUtils.isEmpty(deviceList)){
                    powerBoxLoopService.removeByLoopId(boxLoopId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败,回路有配电箱或者绑定设备");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱回路设备列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/device/list",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDeviceInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object boxLoopIdObj = paramMap.get("boxLoopId");
            if(boxLoopIdObj==null){
                result=Result.paramError(result);
            }else{
                Long boxLoopId=Long.parseLong(boxLoopIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList=powerBoxLoopDeviceService.getDeviceList(boxLoopId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱回路添加设备绑定
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/device/save",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object boxLoopIdObj = paramMap.get("boxLoopId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(boxLoopIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long boxLoopId=Long.parseLong(boxLoopIdObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Boolean flag = powerBoxLoopDeviceService.bindingDevice(boxLoopId, deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/device/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 回路移除设备绑定
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/device/remove",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object boxLoopDeviceIdObj = paramMap.get("boxLoopDeviceId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(boxLoopDeviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long boxLoopDeviceId=Long.parseLong(boxLoopDeviceIdObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Boolean flag=powerBoxLoopDeviceService.unbindingDevice(boxLoopDeviceId,deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/manager/loop/device/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 检验配电箱参数
     * @param paramMap
     * @param powerBox
     * @return
     */
    private String checkPowerBoxParam(Map<String, Object> paramMap,PowerBox powerBox) {
        List<String> errorList=new ArrayList<>();
        Object boxName = paramMap.get("boxName");
        if(boxName==null){
            errorList.add("配电箱名称信息错误");
        }else{
            powerBox.setName(boxName.toString());
        }
        Object loopType = paramMap.get("loopType");
        if(loopType==null){
            errorList.add("配电箱回路类型信息错误");
        }else{
            powerBox.setLoopType(Integer.parseInt(loopType.toString()));
        }
        Object parentTypeObj = paramMap.get("parentType");
        if(parentTypeObj==null){
            errorList.add("父类型信息错误");
        }else{
            Integer parentType=Integer.parseInt(parentTypeObj.toString());
            powerBox.setParentType(parentType);
            if(parentType.equals(0)){
                Object feederIdObj = paramMap.get("feederId");
                if(feederIdObj==null){
                    errorList.add("馈线柜信息错误");
                }else{
                    powerBox.setFeederId(Long.parseLong(feederIdObj.toString()));
                }
                Object feederLoopIdObj = paramMap.get("feederLoopId");
                if(feederLoopIdObj==null){
                    errorList.add("馈线柜回路信息错误");
                }else{
                    powerBox.setFeederLoopId(Long.parseLong(feederLoopIdObj.toString()));
                }
            }else{
                Object boxIdObj = paramMap.get("boxId");
                if(boxIdObj==null){
                    errorList.add("配电箱信息错误");
                }else{
                    powerBox.setBoxId(Long.parseLong(boxIdObj.toString()));
                }
                Object boxLoopIdObj = paramMap.get("boxLoopId");
                if(boxLoopIdObj==null){
                    errorList.add("配电箱回路信息错误");
                }else{
                    powerBox.setBoxLoopId(Long.parseLong(boxLoopIdObj.toString()));
                }
            }
        }
        Object transformerIdObj = paramMap.get("transformerId");
        if(transformerIdObj==null){
            errorList.add("变压器信息错误");
        }else{
            powerBox.setTransformerId(Long.parseLong(transformerIdObj.toString()));
        }
        Object powerIdObj = paramMap.get("powerId");
        if(powerIdObj==null){
            errorList.add("配电房信息错误");
        }else{
            powerBox.setPowerId(Long.parseLong(transformerIdObj.toString()));
        }
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            errorList.add("项目信息错误");
        }else{
            powerBox.setProjectId(Long.parseLong(projectIdObj.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            String join = StringUtils.join(errorList, ",");
            return join;
        }else{
            return null;
        }

    }

    /**
     * 检验元器件参数
     * @param paramMap
     * @param powerComponents
     * @return
     */
    private String checkComponentsParam(Map<String, Object> paramMap, PowerComponents powerComponents) {
        List<String> errorList=new ArrayList<>();
        Object componentsName = paramMap.get("name");
        if(componentsName==null|| org.springframework.util.StringUtils.isEmpty(componentsName.toString())){
            errorList.add("元器件名称信息错误");
        }else{
            powerComponents.setName(componentsName.toString());
        }
        Object componentsBrand = paramMap.get("brand");
        if(componentsBrand==null|| org.springframework.util.StringUtils.isEmpty(componentsBrand.toString())){
            errorList.add("元器件品牌信息错误");
        }else{
            powerComponents.setBrand(componentsBrand.toString());
        }
        Object componentsType = paramMap.get("type");
        if (componentsType == null || org.springframework.util.StringUtils.isEmpty(componentsType.toString())) {
            errorList.add("元器件类型信息错误");
        }else{
            powerComponents.setType(Integer.parseInt(componentsType.toString()));
        }
        Object manufacturer = paramMap.get("manufacturer");
        if(manufacturer==null|| org.springframework.util.StringUtils.isEmpty(manufacturer.toString())){
            errorList.add("生产厂家信息错误");
        }else{
            powerComponents.setManufacturer(manufacturer.toString());
        }
        Object productDate = paramMap.get("productDate");
        if(productDate==null|| org.springframework.util.StringUtils.isEmpty(productDate)){
            errorList.add("生产日期信息错误");
        }else{
            powerComponents.setProductDate(productDate.toString());
        }
        Object usefulLife = paramMap.get("usefulLife");
        if(usefulLife==null|| org.springframework.util.StringUtils.isEmpty(usefulLife.toString())){
            errorList.add("使用寿命信息错误");
        }else{
            powerComponents.setUsefulLife(usefulLife.toString());
        }
        Object effectiveDate = paramMap.get("effectiveDate");
        if(effectiveDate==null|| org.springframework.util.StringUtils.isEmpty(effectiveDate.toString())){
            errorList.add("质保期信息错误");
        }else {
            powerComponents.setEffectiveDate(effectiveDate.toString());
        }
        Object installationCompanyId = paramMap.get("installationCompanyId");
        if(installationCompanyId==null|| org.springframework.util.StringUtils.isEmpty(installationCompanyId)){
            errorList.add("安装公司信息错误");
        }else{
            if(!installationCompanyId.toString().equals("0")){
                powerComponents.setInstallationCompanyId(Long.parseLong(installationCompanyId.toString()));
            }
        }
        Object useCompanyId = paramMap.get("useCompanyId");
        if(useCompanyId==null|| org.springframework.util.StringUtils.isEmpty(useCompanyId.toString())){
            errorList.add("使用公司信息错误");
        }else{
            if(!useCompanyId.toString().equals("0")){
                powerComponents.setUseCompanyId(Long.parseLong(useCompanyId.toString()));
            }
        }
        Object maintenanceCompanyIdObj = paramMap.get("maintenanceCompanyId");
        if(maintenanceCompanyIdObj==null|| org.springframework.util.StringUtils.isEmpty(maintenanceCompanyIdObj.toString())){
            errorList.add("维保公司信息错误");
        }else{
            if(!maintenanceCompanyIdObj.toString().equals("0")){
                powerComponents.setMaintenanceCompanyId(Long.parseLong(maintenanceCompanyIdObj.toString()));
            }
        }
        Object powerDeviceId = paramMap.get("powerDeviceId");
        if(powerDeviceId==null|| org.springframework.util.StringUtils.isEmpty(powerDeviceId.toString())){
            errorList.add("电房设备信息错误");
        }else{
            powerComponents.setPowerDeviceId(Long.parseLong(powerDeviceId.toString()));
        }
        Object powerId = paramMap.get("powerId");
        if(powerId==null|| org.springframework.util.StringUtils.isEmpty(powerId.toString())){
            errorList.add("电房信息错误");
        }else{
            powerComponents.setPowerId(Long.parseLong(powerId.toString()));
        }
        Object projectId = paramMap.get("projectId");
        if(projectId==null|| org.springframework.util.StringUtils.isEmpty(projectId.toString())){
            errorList.add("项目信息错误");
        }else{
            powerComponents.setProjectId(Long.parseLong(projectId.toString()));
        }
        if(CollectionUtils.isEmpty(errorList)){
            return null;
        }else{
            String join = org.apache.commons.lang3.StringUtils.join(errorList, ",");
            return join;
        }
    }
}
