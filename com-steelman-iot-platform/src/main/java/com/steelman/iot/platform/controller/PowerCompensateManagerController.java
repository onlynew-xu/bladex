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

/**
 * 补偿柜
 */

@RestController
@RequestMapping("/api/power/compensate/manager")
public class PowerCompensateManagerController extends BaseController {
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerCompensateService powerCompensateService;
    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerCompensateDeviceService powerCompensateDeviceService;
    @Resource
    private PowerCompensateComponentsService powerCompensateComponentsService;
    @Resource
    private DeviceService deviceService;
    /**
     * 补偿柜列表信息
     * @param pramMap
     * @return
     */
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String compensateList(@RequestBody Map<String, Object> pramMap) {
        Result<List<PowerCompensate>> result = new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = pramMap.get("transformerId");
            if (transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                List<PowerCompensate> dataList = powerCompensateService.selectByTransformerId(transformerId);
                if (CollectionUtils.isEmpty(dataList)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<PowerCompensate>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 更新补偿柜名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String compensateUpdate(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object compensateIdObj = paramMap.get("compensateId");
            Object compensateNameObj = paramMap.get("compensateName");
            Object transformerIdObj = paramMap.get("transformerId");
            if (compensateIdObj == null || compensateNameObj == null || transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long compensateId = Long.parseLong(compensateIdObj.toString());
                String compensateName = compensateNameObj.toString();
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                PowerCompensate compensateInfo = powerCompensateService.getCompensateInfo(compensateId);
                if(compensateInfo==null){
                    result.setMsg("所对应的补偿柜不存在");
                }else{
                    if (compensateInfo.getName().equals(compensateName)) {
                        result.setMsg("名称信息为改变，请修改后重试");
                    } else {
                        PowerCompensate compensateInfoOld = powerCompensateService.selectByTransformerAndName(compensateName, transformerId);
                        if (compensateInfoOld != null) {
                            result.setMsg("名称信息已存在，请修改后重试");
                        } else {
                            PowerCompensate powerCompensateNew = new PowerCompensate();
                            powerCompensateNew.setId(compensateId);
                            powerCompensateNew.setName(compensateName);
                            powerCompensateNew.setUpdateTime(new Date());
                            powerCompensateService.update(powerCompensateNew);
                            result.setCode(1);
                            result.setData(1);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 添加补偿柜
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String compensateSave(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object compensateNameObj = paramMap.get("compensateName");
            Object transformerIdObj = paramMap.get("transformerId");
            if (compensateNameObj == null || transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                String compensateName = compensateNameObj.toString();
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                PowerCompensate compensateInfoOld = powerCompensateService.selectByTransformerAndName(compensateName, transformerId);
                if (compensateInfoOld != null) {
                    result.setMsg("名称信息已存在，请修改后重试");
                } else {
                    PowerTransformer transformer = powerTransformerService.getTransformerInfo(transformerId);
                    PowerCompensate compensateNew = new PowerCompensate();
                    compensateNew.setName(compensateName);
                    compensateNew.setProjectId(transformer.getProjectId());
                    compensateNew.setTransformerId(transformerId);
                    compensateNew.setPowerId(transformer.getPowerId());
                    compensateNew.setCreateTime(new Date());
                    compensateNew.setUpdateTime(compensateNew.getCreateTime());
                    powerCompensateService.insert(compensateNew);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 删除补偿柜
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/remove", produces = CommonUtils.MediaTypeJSON)
    public String compensateRemove(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object compensateIdObj = paramMap.get("compensateId");
            Object transformerIdObj = paramMap.get("transformerId");
            if (compensateIdObj == null || transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long compensateId = Long.parseLong(compensateIdObj.toString());
                PowerCompensate compensateInfo = powerCompensateService.getCompensateInfo(compensateId);
                List<PowerComponentsDto> powerComponentsDtoList = powerComponentsService.selectComponentsByPowerType(2, compensateId, null, compensateInfo.getProjectId());
                List<PowerDeviceInfo> deviceList = powerCompensateDeviceService.getDeviceList(compensateId);
                List<PowerCompensateComponents> componentsList = powerCompensateComponentsService.getComponentsList(compensateId);
                //判断补偿元件和元器件和绑定设备的数量
                if(CollectionUtils.isEmpty(powerComponentsDtoList)&&CollectionUtils.isEmpty(deviceList)&&CollectionUtils.isEmpty(componentsList)){
                    powerCompensateService.remove(compensateId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败,请先删除补偿柜的元器件或者解绑绑定的是设备");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 元器件详情页信息(包含补偿元件)
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/list",produces = CommonUtils.MediaTypeJSON)
    public String componentsList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerComponentsDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateIdObj = paramMap.get("compensateId");
            Object typeObj = paramMap.get("type");
            if(compensateIdObj==null||typeObj==null){
                result=Result.paramError(result);
            }else{
                //type值 0:全部 1:开关 2:避雷 3:补偿元件
                Long  compensateId=Long.parseLong(compensateIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                List<PowerComponentsDto>  powerComponentsDtoList=powerCompensateService.getComponents(compensateId,type);
                if(CollectionUtils.isEmpty(powerComponentsDtoList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerComponentsDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/components/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerComponentsDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加元器件
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
                powerComponents.setPowerDeviceType(2);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加补偿元件
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/compensate/components/save",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerCompensateComponents powerCompensateComponents=new PowerCompensateComponents();
            String msg=checkCompensateComponentsParam(paramMap,powerCompensateComponents);
            if(StringUtils.isEmpty(msg)){
                powerCompensateComponents.setStatus(0);
                powerCompensateComponents.setCreateTime(new Date());
                powerCompensateComponents.setUpdateTime(powerCompensateComponents.getCreateTime());
                powerCompensateComponentsService.insert(powerCompensateComponents);
                result.setCode(1);
                result.setData(1);
            }else{
                result.setMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/compensate/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 元器件更新信息
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerComponentsDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 元器件更新
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿元件更新信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/compensate/components/update/info",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsUpdateInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerCompensateComponents> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateComponentsIdObj = paramMap.get("compensateComponentsId");
            if(compensateComponentsIdObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateComponentsId=Long.parseLong(compensateComponentsIdObj.toString());
                PowerCompensateComponents powerCompensateComponents=powerCompensateComponentsService.selectById(compensateComponentsId);
                if(powerCompensateComponents!=null){
                    result.setCode(1);
                    result.setData(powerCompensateComponents);
                }else{
                    result=  Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/compensate/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerCompensateComponents>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿元器件更新
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/compensate/components/update",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsUpdate(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Boolean flag=powerCompensateComponentsService.updateInfo(paramMap);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/compensate/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除元器件
     * @param paramMap
     * @return
     */
    @Log("删除元器件")
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除补偿元件
     * @param paramMap
     * @return
     */
    @Log("删除补偿元件")
    @PostMapping(value = "/compensate/components/remove",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateComponentsIdObj = paramMap.get("compensateComponentsId");
            if(compensateComponentsIdObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateComponentsId=Long.parseLong(compensateComponentsIdObj.toString());
                powerCompensateComponentsService.delete(compensateComponentsId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/compensate/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿柜绑定设备列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/list",produces = CommonUtils.MediaTypeJSON)
    public String deviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDeviceInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateIdObj = paramMap.get("compensateId");
            if(compensateIdObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateId=Long.parseLong(compensateIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList=powerCompensateDeviceService.getDeviceList(compensateId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿柜绑定设备
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/save", produces = CommonUtils.MediaTypeJSON)
    public String saveDevice(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateIdObj = paramMap.get("compensateId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(compensateIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateId=  Long.parseLong(compensateIdObj.toString());
                Long deviceId=  Long.parseLong(deviceIdObj.toString());
                Device device = deviceService.findById(deviceId);
                if (device.getBindingStatus() == 1) {
                    result.setMsg("设备已绑定，请修改后重试");
                }else{
                    //完善设备绑定
                    Date date = new Date();
                    Device deviceNew=new Device();
                    deviceNew.setBindingType(2); //绑定进线柜
                    deviceNew.setBindingStatus(1); //被绑定
                    deviceNew.setUpdateTime(date);
                    deviceNew.setId(deviceId);
                    deviceService.update(deviceNew);
                    //添加电房设备绑定
                    PowerCompensate powerCompensate = powerCompensateService.getCompensateInfo(compensateId);
                    PowerCompensateDevice powerCompensateDevice = new PowerCompensateDevice();
                    powerCompensateDevice.setDeviceId(deviceId);
                    powerCompensateDevice.setPowerId(powerCompensate.getPowerId());
                    powerCompensateDevice.setCompensateId(compensateId);
                    powerCompensateDevice.setDeviceId(device.getId());
                    powerCompensateDevice.setProjectId(powerCompensate.getProjectId());
                    powerCompensateDevice.setCreateTime(date);
                    powerCompensateDevice.setUpdateTime(date);
                    powerCompensateDeviceService.insert(powerCompensateDevice);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/device/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿柜设备解绑
     * @param paraMap
     * @return
     */
    @PostMapping(value = "/device/remove", produces = CommonUtils.MediaTypeJSON)
    public String deleteDevice(@RequestBody Map<String,Object> paraMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateDeviceIdObj = paraMap.get("compensateDeviceId");
            Object deviceIdObj = paraMap.get("deviceId");
            if(compensateDeviceIdObj==null||deviceIdObj==null){
                result= Result.paramError(result);
            }else{
                Long incomingDeviceId = Long.parseLong(compensateDeviceIdObj.toString());
                Long deviceId = Long.parseLong(deviceIdObj.toString());
                //删除电房绑定信息
                powerCompensateDeviceService.delete(incomingDeviceId);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/manager/device/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 校验元器件参数
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

    /**
     * 校验补偿元件参数
     * @param paramMap
     * @param powerCompensateComponents
     * @return
     */
    private String checkCompensateComponentsParam(Map<String, Object> paramMap, PowerCompensateComponents powerCompensateComponents) {
        List<String> errorList=new ArrayList<>();
        Object componentsName = paramMap.get("name");
        if(componentsName==null|| StringUtils.isEmpty(componentsName.toString())){
            errorList.add("元器件名称信息错误");
        }else{
            powerCompensateComponents.setName(componentsName.toString());
        }
        Object componentsBrand = paramMap.get("brand");
        if(componentsBrand==null||StringUtils.isEmpty(componentsBrand.toString())){
            errorList.add("元器件品牌信息错误");
        }else{
            powerCompensateComponents.setBrand(componentsBrand.toString());
        }
        Object effectiveDate = paramMap.get("effectiveDate");
        if(effectiveDate==null||StringUtils.isEmpty(effectiveDate.toString())){
            errorList.add("质保期信息错误");
        }else {
            powerCompensateComponents.setEffectiveDate(effectiveDate.toString());
        }
        Object compensateCap = paramMap.get("compensateCap");
        if(compensateCap==null||StringUtils.isEmpty(compensateCap.toString())){
            errorList.add("补偿容量参数信息错误");
        }else {
            powerCompensateComponents.setCompensateCap(compensateCap.toString());
        }
        Object compensateRoad = paramMap.get("compensateRoad");
        if(compensateRoad==null||StringUtils.isEmpty(compensateRoad.toString())){
            errorList.add("补偿回路信息错误");
        }else {
            powerCompensateComponents.setCompensateRoad(compensateRoad.toString());
        }
        Object compensateIdObj = paramMap.get("compensateId");
        if(compensateIdObj==null||StringUtils.isEmpty(compensateIdObj.toString())){
            errorList.add("补偿柜id信息错误");
        }else {
            powerCompensateComponents.setCompensateId(Long.parseLong(compensateIdObj.toString()));
        }
        Object powerId = paramMap.get("powerId");
        if(powerId==null||StringUtils.isEmpty(powerId.toString())){
            errorList.add("电房信息错误");
        }else{
            powerCompensateComponents.setPowerId(Long.parseLong(powerId.toString()));
        }
        Object projectId = paramMap.get("projectId");
        if(projectId==null||StringUtils.isEmpty(projectId.toString())){
            errorList.add("项目信息错误");
        }else{
            powerCompensateComponents.setProjectId(Long.parseLong(projectId.toString()));
        }
        if(CollectionUtils.isEmpty(errorList)){
            return null;
        }else{
            String join = org.apache.commons.lang3.StringUtils.join(errorList, ",");
            return join;
        }

    }

}
