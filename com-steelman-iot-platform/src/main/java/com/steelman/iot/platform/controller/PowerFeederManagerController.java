package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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

@RestController
@RequestMapping("/api/power/feeder/manager")
public class PowerFeederManagerController extends BaseController {
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerFeederLoopService powerFeederLoopService;
    @Resource
    private PowerBoxService powerBoxService;
    @Resource
    private PowerFeederLoopDeviceService powerFeederLoopDeviceService;


    /**
     * 单回路馈线柜列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/single/list",produces = CommonUtils.MediaTypeJSON)
    public String singleList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerFeeder>> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long transformerId= Long.parseLong(transformerIdObj.toString());
                List<PowerFeeder> powerFeeders=powerFeederService.getPowerFeederType(transformerId,0);
                if(CollectionUtils.isEmpty(powerFeeders)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerFeeders);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/single/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerFeeder>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 多回路馈线柜列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/multiply/list",produces = CommonUtils.MediaTypeJSON)
    public String multiplyList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerFeeder>> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long transformerId= Long.parseLong(transformerIdObj.toString());
                List<PowerFeeder> powerFeeders=powerFeederService.getPowerFeederType(transformerId,1);
                if(CollectionUtils.isEmpty(powerFeeders)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerFeeders);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/multiply/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerFeeder>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加馈线柜
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save",produces = CommonUtils.MediaTypeJSON)
    public String singleSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object feederNameObj = paramMap.get("feederName");
            Object typeObj = paramMap.get("type");
            if(transformerIdObj==null||feederNameObj==null||typeObj==null){
                result=Result.paramError(result);
            }else{
                Long transformerId= Long.parseLong(transformerIdObj.toString());
                String feederName= feederNameObj.toString();
                Integer feedType= Integer.parseInt(typeObj.toString());
                Boolean flag=powerFeederService.save(transformerId,feederName,feedType);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改馈线柜名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update",produces = CommonUtils.MediaTypeJSON)
    public String update(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            Object feederNameObj = paramMap.get("feederName");
            if(feederNameObj==null||feederIdObj==null){
                result=Result.paramError(result);
            }else{
                String feederName= feederNameObj.toString();
                Long feederId= Long.parseLong(feederIdObj.toString());
                PowerFeeder feederInfo = powerFeederService.getFeederInfo(feederId);
                Long powerId = feederInfo.getPowerId();
                PowerFeeder powerFeederOld=powerFeederService.selectByPowerIdAndName(feederName,powerId);
                if(powerFeederOld!=null){
                    result.setMsg("名称已存在,请修改后重试");
                }else{
                    PowerFeeder powerFeeder=new PowerFeeder();
                    powerFeeder.setId(feederId);
                    powerFeeder.setName(feederName);
                    powerFeeder.setUpdateTime(new Date());
                    powerFeederService.update(powerFeeder);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除馈线柜
     * @param paramMap
     * @return
     */
    @Log("删除馈线柜")
    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String remove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            if(feederIdObj==null){
                result=Result.paramError(result);
            }else{
                Long feederId= Long.parseLong(feederIdObj.toString());
                PowerFeeder feederInfo = powerFeederService.getFeederInfo(feederId);
                List<PowerComponentsDto> powerComponentsDtoList = powerComponentsService.selectComponentsByPowerType(4,feederId,0,feederInfo.getProjectId());
                List<PowerFeederLoop> powerFeederLoopList = powerFeederLoopService.getFeederLoopByFeederId(feederId);
                List<PowerBox> powerBoxList=new ArrayList<>();
                List<PowerDeviceInfo> powerBoxLoopDevices=new ArrayList<>();
                if(!CollectionUtils.isEmpty(powerFeederLoopList)){
                    for (PowerFeederLoop powerFeederLoop : powerFeederLoopList) {
                        Long feederLoopId = powerFeederLoop.getId();
                        List<PowerBox> powerBoxes = powerBoxService.getBoxListByLoopId(feederLoopId, 0);
                        List<PowerDeviceInfo> powerBoxLoopDeviceList = powerFeederLoopDeviceService.getDeviceList(feederLoopId) ;
                        if(!CollectionUtils.isEmpty(powerBoxes)){
                            powerBoxList.addAll(powerBoxes);
                        }
                        if(!CollectionUtils.isEmpty(powerBoxLoopDeviceList)){
                            powerBoxLoopDevices.addAll(powerBoxLoopDeviceList);
                        }
                    }
                }
                if(CollectionUtils.isEmpty(powerComponentsDtoList)
                && CollectionUtils.isEmpty(powerBoxList) && CollectionUtils.isEmpty(powerBoxLoopDevices)){
                    powerFeederService.removeFeeder(feederId);
                    powerFeederLoopService.removeByFeederId(feederId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败，馈线柜还有回路或元器件未删除");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜元器件列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/list",produces = CommonUtils.MediaTypeJSON)
    public String componentsList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerComponentsDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            Object projectIdObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if(feederIdObj==null||projectIdObj==null||typeObj==null){
                result= Result.paramError(result);
            }else{
                Long  feederId=Long.parseLong(feederIdObj.toString());
                Long  projectId=Long.parseLong(projectIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                if(type.equals(0)){
                    type=null;
                }
                List<PowerComponentsDto> list = powerComponentsService.selectComponentsByPowerType(4,feederId,type,projectId);
                if(CollectionUtils.isEmpty(list)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/components/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerComponentsDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜添加元器件
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
            if(StringUtils.isEmpty(msg)){
                powerComponents.setPowerDeviceType(4);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜元器件修改返回信息
     * @param paramMap
     * @return
     */
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerComponentsDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



    /**
     * 馈线柜修改元器件属性
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜删除元器件
     * @param paramMap
     * @return
     */
    @Log("删除馈线柜元器件")
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜回路列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/list",produces = CommonUtils.MediaTypeJSON)
    public String loopList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerFeederLoop>> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            if(feederIdObj==null){
                result=Result.paramError(result);
            }else{
                Long feederId=Long.parseLong(feederIdObj.toString());
                List<PowerFeederLoop> powerFeederLoopList = powerFeederLoopService.getFeederLoopByFeederId(feederId);
                if(CollectionUtils.isEmpty(powerFeederLoopList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerFeederLoopList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜添加回路
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/save",produces = CommonUtils.MediaTypeJSON)
    public String loopSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            Object loopNameObj = paramMap.get("loopName");
            if(feederIdObj==null||loopNameObj==null){
                result=Result.paramError(result);
            }else{
                Long feederId=Long.parseLong(feederIdObj.toString());
                String loopName=loopNameObj.toString();
                PowerFeederLoop powerFeederLoopOld = powerFeederLoopService.findByFeederIdAndLoop(feederId,loopName);
                if(powerFeederLoopOld==null){
                    PowerFeeder feederInfo = powerFeederService.getFeederInfo(feederId);
                    PowerFeederLoop powerFeederLoopNew=new PowerFeederLoop();
                    powerFeederLoopNew.setTotalFlag(0);
                    powerFeederLoopNew.setName(loopName);
                    powerFeederLoopNew.setProjectId(feederInfo.getProjectId());
                    powerFeederLoopNew.setPowerId(feederInfo.getPowerId());
                    powerFeederLoopNew.setFeederId(feederId);
                    powerFeederLoopNew.setCreateTime(new Date());
                    powerFeederLoopNew.setUpdateTime(powerFeederLoopNew.getCreateTime());
                    powerFeederLoopService.insert(powerFeederLoopNew);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("名称已存在,请修改后重试");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜修改回路名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/update",produces = CommonUtils.MediaTypeJSON)
    public String loopUpdate(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("feederLoopId");
            Object nameObj = paramMap.get("feederLoopName");
            if(idObj==null||nameObj==null){
                result=Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                String name=nameObj.toString();
                PowerFeederLoop feederLoopInfo = powerFeederLoopService.getFeederLoopInfo(id);
                if(feederLoopInfo.getName().equals(name)){
                    result.setMsg("名称为改变,请修改后重试");
                }else{
                    PowerFeederLoop powerFeederLoopOld = powerFeederLoopService.findByFeederIdAndLoop(feederLoopInfo.getFeederId(),name);
                    if(powerFeederLoopOld==null){
                        PowerFeederLoop powerFeederLoopNew=new PowerFeederLoop();
                        powerFeederLoopNew.setName(name);
                        powerFeederLoopNew.setId(id);
                        powerFeederLoopNew.setUpdateTime(new Date());
                        powerFeederLoopService.update(powerFeederLoopNew);
                        result.setCode(1);
                        result.setData(1);
                    }else{
                        result.setMsg("名称已存在,请修改后重试");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 馈线柜删除回路
     * @param paramMap
     * @return
     */
    @Log("馈线柜删除回路")
    @PostMapping(value = "/loop/remove",produces = CommonUtils.MediaTypeJSON)
    public String loopRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("feederLoopId");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                List<PowerBox> powerBoxList=powerBoxService.getBoxListByLoopId(id,0);
                List<PowerDeviceInfo> deviceList = powerFeederLoopDeviceService.getDeviceList(id);
                if(CollectionUtils.isEmpty(powerBoxList)&& CollectionUtils.isEmpty(deviceList)){
                    powerFeederLoopService.removeFeederLoop(id);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败,回路有配电箱或者绑定设备");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 回路设备列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/device/list",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDeviceInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object feederLoopIdObj = paramMap.get("feederLoopId");
            if(feederLoopIdObj==null){
                result=Result.paramError(result);
            }else{
                Long feederLoopId=Long.parseLong(feederLoopIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList=powerFeederLoopDeviceService.getDeviceList(feederLoopId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 回路添加设备绑定
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/loop/device/save",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object feederLoopIdObj = paramMap.get("feederLoopId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(feederLoopIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long feederLoopId=Long.parseLong(feederLoopIdObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Boolean flag = powerFeederLoopDeviceService.bindingDevice(feederLoopId, deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/device/save"));
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
    @Log("回路解绑设备")
    @PostMapping(value = "/loop/device/remove",produces = CommonUtils.MediaTypeJSON)
    public String loopDeviceRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object feederLoopDeviceIdObj = paramMap.get("feederLoopDeviceId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(feederLoopDeviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long feederLoopDeviceId=Long.parseLong(feederLoopDeviceIdObj.toString());
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Boolean flag=powerFeederLoopDeviceService.unbindingDevice(feederLoopDeviceId,deviceId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/manager/loop/device/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
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
        if(componentsName==null|| StringUtils.isEmpty(componentsName.toString())){
            errorList.add("元器件名称信息错误");
        }else{
            powerComponents.setName(componentsName.toString());
        }
        Object componentsBrand = paramMap.get("brand");
        if(componentsBrand==null||StringUtils.isEmpty(componentsBrand.toString())){
            errorList.add("元器件品牌信息错误");
        }else{
            powerComponents.setBrand(componentsBrand.toString());
        }
        Object componentsType = paramMap.get("type");
        if (componentsType == null || StringUtils.isEmpty(componentsType.toString())) {
            errorList.add("元器件类型信息错误");
        }else{
            powerComponents.setType(Integer.parseInt(componentsType.toString()));
        }
        Object manufacturer = paramMap.get("manufacturer");
        if(manufacturer==null||StringUtils.isEmpty(manufacturer.toString())){
            errorList.add("生产厂家信息错误");
        }else{
            powerComponents.setManufacturer(manufacturer.toString());
        }
        Object productDate = paramMap.get("productDate");
        if(productDate==null||StringUtils.isEmpty(productDate)){
            errorList.add("生产日期信息错误");
        }else{
            powerComponents.setProductDate(productDate.toString());
        }
        Object usefulLife = paramMap.get("usefulLife");
        if(usefulLife==null||StringUtils.isEmpty(usefulLife.toString())){
            errorList.add("使用寿命信息错误");
        }else{
            powerComponents.setUsefulLife(usefulLife.toString());
        }
        Object effectiveDate = paramMap.get("effectiveDate");
        if(effectiveDate==null||StringUtils.isEmpty(effectiveDate.toString())){
            errorList.add("质保期信息错误");
        }else {
            powerComponents.setEffectiveDate(effectiveDate.toString());
        }
        Object installationCompanyId = paramMap.get("installationCompanyId");
        if(installationCompanyId==null||StringUtils.isEmpty(installationCompanyId)){
            errorList.add("安装公司信息错误");
        }else{
            if(!installationCompanyId.toString().equals("0")){
                powerComponents.setInstallationCompanyId(Long.parseLong(installationCompanyId.toString()));
            }
        }
        Object useCompanyId = paramMap.get("useCompanyId");
        if(useCompanyId==null||StringUtils.isEmpty(useCompanyId.toString())){
            errorList.add("使用公司信息错误");
        }else{
            if(!useCompanyId.toString().equals("0")){
                powerComponents.setUseCompanyId(Long.parseLong(useCompanyId.toString()));
            }
        }
        Object maintenanceCompanyIdObj = paramMap.get("maintenanceCompanyId");
        if(maintenanceCompanyIdObj==null||StringUtils.isEmpty(maintenanceCompanyIdObj.toString())){
            errorList.add("维保公司信息错误");
        }else{
            if(!maintenanceCompanyIdObj.toString().equals("0")){
                powerComponents.setMaintenanceCompanyId(Long.parseLong(maintenanceCompanyIdObj.toString()));
            }
        }
        Object powerDeviceId = paramMap.get("powerDeviceId");
        if(powerDeviceId==null||StringUtils.isEmpty(powerDeviceId.toString())){
            errorList.add("电房设备信息错误");
        }else{
            powerComponents.setPowerDeviceId(Long.parseLong(powerDeviceId.toString()));
        }
        Object powerId = paramMap.get("powerId");
        if(powerId==null||StringUtils.isEmpty(powerId.toString())){
            errorList.add("电房信息错误");
        }else{
            powerComponents.setPowerId(Long.parseLong(powerId.toString()));
        }
        Object projectId = paramMap.get("projectId");
        if(projectId==null||StringUtils.isEmpty(projectId.toString())){
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
