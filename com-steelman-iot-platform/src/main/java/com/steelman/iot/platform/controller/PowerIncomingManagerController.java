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
@RequestMapping("/api/power/incoming/manager")
public class PowerIncomingManagerController extends BaseController {
    @Resource
    private PowerIncomingService powerIncomingService;
    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private AlarmWarnService alarmWarnService;

    /**
     * 获取进线柜列表信息
     * @param paramMap
     * @return
     */
    @Log("获取进线柜列表信息")
    @PostMapping(value = "/list",produces = CommonUtils.MediaTypeJSON)
    public String incomingList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerIncoming>> result=new Result<>();
        result.setCode(0);
        try {
            Object transformIdObj = paramMap.get("transformerId");
            if(transformIdObj==null){
               result= Result.paramError(result);
            }else{
                Long  transformId=Long.parseLong(transformIdObj.toString());
                List<PowerIncoming> list = powerIncomingService.getIncomingList(transformId);
                if(CollectionUtils.isEmpty(list)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result< List<PowerIncoming>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加进线柜信息
     * @param paramMap
     * @return
     */
    @Log("添加进线柜信息")
    @PostMapping(value = "/save",produces = CommonUtils.MediaTypeJSON)
    public String saveIncoming(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object incomingNameObj = paramMap.get("incomingName");
            if(transformerIdObj==null||incomingNameObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                String incomingName=incomingNameObj.toString();
                PowerIncoming powerIncoming= powerIncomingService.selectByName(incomingName,transformerId);
                if(powerIncoming!=null){
                    result.setMsg("名称信息请存在，请修改后重试");
                }else{
                    PowerTransformer transformerInfo = powerTransformerService.getTransformerInfo(transformerId);
                    PowerIncoming powerIncomingNew = new PowerIncoming();
                    powerIncomingNew.setName(incomingName);
                    powerIncomingNew.setTransformerId(transformerId);
                    powerIncomingNew.setPowerId(transformerInfo.getPowerId());
                    powerIncomingNew.setProjectId(transformerInfo.getProjectId());
                    Date date=new Date();
                    powerIncomingNew.setCreateTime(date);
                    powerIncomingNew.setUpdateTime(date);
                    powerIncomingService.insert(powerIncomingNew);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 修改进线柜名称
     * @param paramMap
     * @return
     */
    @Log("修改进线柜名称")
    @PostMapping(value = "/update",produces = CommonUtils.MediaTypeJSON)
    public String updateIncoming(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            Object incomingNameObj = paramMap.get("incomingName");
            Object transformerIdObj = paramMap.get("transformerId");
            if (incomingIdObj == null || incomingNameObj == null || transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long incomingId = Long.parseLong(incomingIdObj.toString());
                String incomingName = incomingNameObj.toString();
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                PowerIncoming incomingInfo = powerIncomingService.getIncomingInfo(incomingId);
                if (incomingInfo.getName().equals(incomingName)) {
                    result.setMsg("名称信息未改变，请修改后重试");
                } else {
                    PowerIncoming powerIncoming = powerIncomingService.selectByName(incomingName, transformerId);
                    if (powerIncoming != null) {
                        result.setMsg("名称信息已存在，请修改后重试");
                    } else {
                        PowerIncoming powerIncomingOld = new PowerIncoming();
                        powerIncomingOld.setId(incomingId);
                        powerIncomingOld.setName(incomingName);
                        powerIncomingOld.setUpdateTime(new Date());
                        powerIncomingService.update(powerIncomingOld);
                        result.setCode(1);
                        result.setData(1);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 删除进线柜
     * @param paramMap
     * @return
     */
    @Log("删除进线柜")
    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String deleteIncoming(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            Object transformIdObj = paramMap.get("transformerId");
            if(incomingIdObj==null||transformIdObj==null){
               result= Result.paramError(result);
            }else{
                //检验元器件个设备
                Long incomingId=Long.parseLong(incomingIdObj.toString());
                PowerIncoming incomingInfo = powerIncomingService.getIncomingInfo(incomingId);
                if(incomingInfo==null){
                    result.setMsg("进线柜信息不存在");
                }else{
                    List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(incomingId);
                    List<PowerComponentsDto> powerComponentsDtoList = powerComponentsService.selectComponentsByPowerType(1, incomingId, null, incomingInfo.getProjectId());
                    if(CollectionUtils.isEmpty(deviceList)&&CollectionUtils.isEmpty(powerComponentsDtoList)){
                        powerIncomingService.delete(incomingId);
                        result.setCode(1);
                        result.setData(1);
                    }else{
                        result.setMsg("请删除元器件并且解绑所有设备");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 进线柜元器件列表信息
     * @param paramMap
     * @return
     */
    @Log("获取进线柜的元器件")
    @PostMapping(value = "/components/list",produces = CommonUtils.MediaTypeJSON)
    public String componentsList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerComponentsDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            Object projectIdObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if(incomingIdObj==null||projectIdObj==null||typeObj==null){
                result= Result.paramError(result);
            }else{
                Long  incomingId=Long.parseLong(incomingIdObj.toString());
                Long  projectId=Long.parseLong(projectIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                if(type.equals(0)){
                    type=null;
                }
                List<PowerComponentsDto> list = powerComponentsService.selectComponentsByPowerType(1,incomingId,type,projectId);
                if(CollectionUtils.isEmpty(list)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/components/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerComponentsDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜添加元器件
     * @param paramMap
     * @return
     */
    @Log("进线柜添加元器件")
    @PostMapping(value = "/components/save",produces = CommonUtils.MediaTypeJSON)
    public String componentsSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerComponents powerComponents=new PowerComponents();
            String msg=checkComponentsParam(paramMap,powerComponents);
            if(StringUtils.isEmpty(msg)){
                powerComponents.setPowerDeviceType(1);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜元器件修改返回的信息
     * @param paramMap
     * @return
     */
    @Log("修改返回的元器件的信息")
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerComponentsDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜修改元器件信息
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜删除元器件
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜绑定设备列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/list",produces = CommonUtils.MediaTypeJSON)
    public String deviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDeviceInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            if(incomingIdObj==null){
                result=Result.paramError(result);
            }else{
                Long incomingId=Long.parseLong(incomingIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList=powerIncomingDeviceService.getDeviceList(incomingId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜绑定设备
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/save", produces = CommonUtils.MediaTypeJSON)
    public String saveDevice(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(incomingIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long incomingId=  Long.parseLong(incomingIdObj.toString());
                Long deviceId=  Long.parseLong(deviceIdObj.toString());
                Device device = deviceService.findById(deviceId);
                if (device.getBindingStatus() == 1) {
                    result.setMsg("设备已绑定，请修改后重试");
                }else{
                    //完善设备绑定
                    Date date = new Date();
                    Device deviceNew=new Device();
                    deviceNew.setBindingType(1); //绑定进线柜
                    deviceNew.setBindingStatus(1); //被绑定
                    deviceNew.setUpdateTime(date);
                    deviceNew.setId(deviceId);
                    deviceService.update(deviceNew);
                    //添加电房设备绑定
                    PowerIncoming incomingInfo = powerIncomingService.getIncomingInfo(incomingId);
                    PowerIncomingDevice powerIncomingDevice = new PowerIncomingDevice();
                    powerIncomingDevice.setDeviceId(deviceId);
                    powerIncomingDevice.setPowerId(incomingInfo.getPowerId());
                    powerIncomingDevice.setIncomingId(incomingId);
                    powerIncomingDevice.setDeviceId(device.getId());
                    powerIncomingDevice.setProjectId(incomingInfo.getProjectId());
                    powerIncomingDevice.setCreateTime(date);
                    powerIncomingDevice.setUpdateTime(date);
                    powerIncomingDeviceService.insert(powerIncomingDevice);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/device/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜删除绑定的设备
     * @param paraMap
     * @return
     */
    @PostMapping(value = "/device/remove", produces = CommonUtils.MediaTypeJSON)
    public String deleteDevice(@RequestBody Map<String,Object> paraMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingDeviceIdObj = paraMap.get("incomingDeviceId");
            Object deviceIdObj = paraMap.get("deviceId");
            if(incomingDeviceIdObj==null||deviceIdObj==null){
                result= Result.paramError(result);
            }else{
                Long incomingDeviceId = Long.parseLong(incomingDeviceIdObj.toString());
                Long deviceId = Long.parseLong(deviceIdObj.toString());
                //删除电房绑定信息
                powerIncomingDeviceService.delete(incomingDeviceId);
                //更新设备绑定状态
                Device device=new Device();
                device.setId(deviceId);
                device.setBindingStatus(0);
                device.setBindingType(-1);
                device.setUpdateTime(new Date());
                deviceService.update(device);
                alarmWarnService.removePowerAlarm(deviceId);
                result.setCode(1);
                result.setData(1);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/manager/device/remove"));
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
