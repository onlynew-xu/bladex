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
@RequestMapping("/api/power/wave/manager")
public class PowerWaveManagerController extends BaseController {
    @Resource
    private PowerWaveService powerWaveService;
    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerWaveDeviceService powerWaveDeviceService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerWaveComponentsService powerWaveComponentsService;

    /**
     * 滤波柜列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/list",produces = CommonUtils.MediaTypeJSON)
    public String waveList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerWave>> result =new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                List<PowerWave> list = powerWaveService.selectByTransformerId(transformerId);
                if(CollectionUtils.isEmpty(list)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerWave>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加滤波柜
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save",produces = CommonUtils.MediaTypeJSON)
    public String saveWave(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object waveNameObj = paramMap.get("waveName");
            if(transformerIdObj==null||waveNameObj==null){
              result= Result.paramError(result);
            }else{
                Long transformerId= Long.parseLong(transformerIdObj.toString());
                String waveName=waveNameObj.toString();
                PowerWave powerWave= powerWaveService.selectByName(transformerId,waveName);
                if(powerWave!=null){
                    result.setMsg("名称信息已存在，请修改后重试");
                }else{
                    PowerTransformer transformer = powerTransformerService.getTransformerInfo(transformerId);
                    PowerWave wave = new PowerWave();
                    wave.setName(waveName);
                    wave.setTransformerId(transformerId);
                    wave.setPowerId(transformer.getPowerId());
                    wave.setProjectId(transformer.getProjectId());
                    wave.setCreateTime(new Date());
                    wave.setUpdateTime(wave.getCreateTime());
                    powerWaveService.insert(wave);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改滤波柜名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String updateWave(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object waveNameObj = paramMap.get("waveName");
            Object waveIdObj = paramMap.get("waveId");
            if(transformerIdObj==null||waveNameObj==null||waveIdObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                String waveName=waveNameObj.toString();
                Long waveId=Long.parseLong(waveIdObj.toString());
                PowerWave waveInfo = powerWaveService.getWaveInfo(waveId);
                if(waveInfo.getName().equals(waveName)){
                    result.setMsg("名称信息未改变，请修改后重试");
                }else{
                    PowerWave powerWave = powerWaveService.selectByName(transformerId, waveName);
                    if(powerWave!=null){
                        result.setMsg("名称信息已改变，请修改后重试");
                    }else{
                        PowerWave wave = new PowerWave();
                        wave.setId(waveId);
                        wave.setName(waveName);
                        wave.setUpdateTime(new Date());
                        powerWaveService.update(wave);
                        result.setCode(1);
                        result.setData(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除滤波柜
     * @param paramMap
     * @return
     */
    @Log("删除滤波柜")
    @PostMapping(value = "/remove", produces = CommonUtils.MediaTypeJSON)
    public String removeWave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object waveIdObj = paramMap.get("waveId");
            if(waveIdObj==null||transformerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long waveId=Long.parseLong(waveIdObj.toString());
                List<PowerDeviceInfo> deviceList = powerWaveDeviceService.getDeviceList(waveId);
                List<PowerWaveComponents> componentsList = powerWaveComponentsService.getComponentsList(waveId);
                if(CollectionUtils.isEmpty(deviceList)&&CollectionUtils.isEmpty(componentsList)){
                    powerWaveService.deleteById(waveId);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 元器件详情页信息(包含滤波元件)
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/components/list",produces = CommonUtils.MediaTypeJSON)
    public String componentsList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerComponentsDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object waveIdObj = paramMap.get("waveId");
            Object typeObj = paramMap.get("type");
            if(waveIdObj==null||typeObj==null){
                result=Result.paramError(result);
            }else{
                Long  waveId=Long.parseLong(waveIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                List<PowerComponentsDto>  powerComponentsDtoList=powerWaveService.getComponents(waveId,type);
                if(CollectionUtils.isEmpty(powerComponentsDtoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerComponentsDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/components/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerComponentsDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 保存元器件
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
                powerComponents.setPowerDeviceType(3);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 保存滤波元件
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/wave/components/save",produces = CommonUtils.MediaTypeJSON)
    public String waveComponentsSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerWaveComponents powerWaveComponents=new PowerWaveComponents();
            String msg=checkWaveComponentsParam(paramMap,powerWaveComponents);
            if(StringUtils.isEmpty(msg)){
                powerWaveComponents.setStatus(0);
                powerWaveComponents.setCreateTime(new Date());
                powerWaveComponents.setUpdateTime(powerWaveComponents.getCreateTime());
                powerWaveComponentsService.insert(powerWaveComponents);
                result.setCode(1);
                result.setData(1);
            }else{
                result.setMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/wave/components/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 元器件更新修改信息
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/components/update/info"));
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/components/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 滤波元件更新信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/wave/components/update/info",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsUpdateInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerWaveComponents> result=new Result<>();
        result.setCode(0);
        Object waveComponentsIdObj = paramMap.get("waveComponentsId");
        try {
            if(waveComponentsIdObj==null){
                result=Result.paramError(result);
            }else{
                Long waveComponentsId=Long.parseLong(waveComponentsIdObj.toString());
                PowerWaveComponents powerWaveComponents=powerWaveComponentsService.selectById(waveComponentsId);
                if(powerWaveComponents!=null){
                    result.setCode(1);
                    result.setData(powerWaveComponents);
                }else{
                    result=  Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/wave/components/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerWaveComponents>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 滤波元件更新
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/wave/components/update",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsUpdate(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Boolean flag=powerWaveComponentsService.updateInfo(paramMap);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/wave/components/update"));
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 滤波元件删除
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/wave/components/remove",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object waveComponentsIdObj = paramMap.get("waveComponentsId");
            if(waveComponentsIdObj==null){
                result=Result.paramError(result);
            }else{
                Long waveComponentsId=Long.parseLong(waveComponentsIdObj.toString());
                powerWaveComponentsService.delete(waveComponentsId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/wave/components/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 绑定设备列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/list",produces = CommonUtils.MediaTypeJSON)
    public String deviceList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDeviceInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object waveIdObj = paramMap.get("waveId");
            if(waveIdObj==null){
                result=Result.paramError(result);
            }else{
                Long waveId=Long.parseLong(waveIdObj.toString());
                List<PowerDeviceInfo> powerDeviceInfoList=powerWaveDeviceService.getDeviceList(waveId);
                if(CollectionUtils.isEmpty(powerDeviceInfoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/device/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDeviceInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 设备绑定
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/save", produces = CommonUtils.MediaTypeJSON)
    public String saveDevice(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object waveIdObj = paramMap.get("waveId");
            Object deviceIdObj = paramMap.get("deviceId");
            if(waveIdObj==null||deviceIdObj==null){
                result=Result.paramError(result);
            }else{
                Long waveId=  Long.parseLong(waveIdObj.toString());
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
                    PowerWave powerWave = powerWaveService.getWaveInfo(waveId);
                    PowerWaveDevice powerWaveDevice = new PowerWaveDevice();
                    powerWaveDevice.setDeviceId(deviceId);
                    powerWaveDevice.setPowerId(powerWave.getPowerId());
                    powerWaveDevice.setWaveId(waveId);
                    powerWaveDevice.setDeviceId(device.getId());
                    powerWaveDevice.setProjectId(powerWave.getProjectId());
                    powerWaveDevice.setCreateTime(date);
                    powerWaveDevice.setUpdateTime(date);
                    powerWaveDeviceService.insert(powerWaveDevice);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/device/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 设备解绑
     * @param paraMap
     * @return
     */
    @PostMapping(value = "/device/remove", produces = CommonUtils.MediaTypeJSON)
    public String deleteDevice(@RequestBody Map<String,Object> paraMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object waveDeviceIdObj = paraMap.get("waveDeviceId");
            Object deviceIdObj = paraMap.get("deviceId");
            if(waveDeviceIdObj==null||deviceIdObj==null){
                result= Result.paramError(result);
            }else{
                Long waveDeviceId = Long.parseLong(waveDeviceIdObj.toString());
                Long deviceId = Long.parseLong(deviceIdObj.toString());
                //删除电房绑定信息
                powerWaveDeviceService.delete(waveDeviceId);
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/manager/device/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

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

    private String checkWaveComponentsParam(Map<String, Object> paramMap, PowerWaveComponents powerWaveComponents) {
        List<String> errorList=new ArrayList<>();
        Object componentsName = paramMap.get("name");
        if(componentsName==null|| StringUtils.isEmpty(componentsName.toString())){
            errorList.add("元器件名称信息错误");
        }else{
            powerWaveComponents.setName(componentsName.toString());
        }
        Object componentsBrand = paramMap.get("brand");
        if(componentsBrand==null||StringUtils.isEmpty(componentsBrand.toString())){
            errorList.add("元器件品牌信息错误");
        }else{
            powerWaveComponents.setBrand(componentsBrand.toString());
        }
        Object effectiveDate = paramMap.get("effectiveDate");
        if(effectiveDate==null||StringUtils.isEmpty(effectiveDate.toString())){
            errorList.add("质保期信息错误");
        }else {
            powerWaveComponents.setEffectiveDate(effectiveDate.toString());
        }

        Object waveIdObj = paramMap.get("waveId");
        if(waveIdObj==null||StringUtils.isEmpty(waveIdObj.toString())){
            errorList.add("滤波柜信息错误");
        }else {
            powerWaveComponents.setWaveId(Long.parseLong(waveIdObj.toString()));
        }
        Object powerId = paramMap.get("powerId");
        if(powerId==null||StringUtils.isEmpty(powerId.toString())){
            errorList.add("电房信息错误");
        }else{
            powerWaveComponents.setPowerId(Long.parseLong(powerId.toString()));
        }
        Object projectId = paramMap.get("projectId");
        if(projectId==null||StringUtils.isEmpty(projectId.toString())){
            errorList.add("项目信息错误");
        }else{
            powerWaveComponents.setProjectId(Long.parseLong(projectId.toString()));
        }
        if(CollectionUtils.isEmpty(errorList)){
            return null;
        }else{
            String join = org.apache.commons.lang3.StringUtils.join(errorList, ",");
            return join;
        }

    }

}
