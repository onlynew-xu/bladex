package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Area;
import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.entity.Room;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.RegionAreaService;
import com.steelman.iot.platform.service.RegionBuildingService;
import com.steelman.iot.platform.service.RegionRoomService;
import com.steelman.iot.platform.service.RegionStoreyService;
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
@RequestMapping("/api/region/manager")
public class RegionManagerController extends BaseController {
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;

    @PostMapping(value = "/areaList",produces = CommonUtils.MediaTypeJSON)
    public String regionAreaList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<Area> areaList = regionAreaService.selectByProjectId(projectId);
                if(CollectionUtils.isEmpty(areaList)){
                    result=Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Area area : areaList) {
                        dataList.add(new EntityDto(area.getId(),area.getName(),area.getProjectId()));
                    }
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/areaList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 保存区域信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save/area",produces = CommonUtils.MediaTypeJSON)
    public String saveRegionArea(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object areaNameObj = paramMap.get("areaName");
            Object projectIdObj = paramMap.get("projectId");
            if(areaNameObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                String areaName=areaNameObj.toString();
                Long projectId=Long.parseLong(projectIdObj.toString());
                Area area=regionAreaService.selectByAreaName(areaName,projectId);
                if(area!=null){
                    result.setMsg("名称已存在");
                }else{
                    Area newArea = new Area();
                    newArea.setName(areaName);
                    newArea.setProjectId(projectId);
                    Date date=new Date();
                    newArea.setCreateTime(date);
                    newArea.setUpdateTime(date);
                    regionAreaService.insert(newArea);
                    result.setData(1);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/save/area"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改区域名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update/area",produces = CommonUtils.MediaTypeJSON)
    public String updateRegionArea(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object areaIdObj = paramMap.get("areaId");
            Object projectIdObj = paramMap.get("projectId");
            Object areaNameObj = paramMap.get("areaName");
            if(areaIdObj==null||areaNameObj==null||projectIdObj==null){
                result= Result.paramError(result);
            }else{
                String areaName=areaNameObj.toString();
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long areaId=Long.parseLong(areaIdObj.toString());
                Area area1 = regionAreaService.selectByPrimaryKey(areaId);
                if(area1.getName().equals(areaName)){
                    result.setMsg("区域名称信息未改变，请修改后重试");
                }else{
                    Area areal = regionAreaService.selectByAreaName(areaName, projectId);
                    if(areal!=null){
                        result.setMsg("区域名称信息已存在，请修改后重试");
                    }else{
                        Date date = new Date();
                        Area updateArea = new Area();
                        updateArea.setName(areaName);
                        updateArea.setId(areaId);
                        updateArea.setUpdateTime(date);
                        regionAreaService.update(updateArea);
                        result.setData(1);
                        result.setCode(1);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/update/area"));
            result=Result.exceptionRe(result);

        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/delete/area",produces = CommonUtils.MediaTypeJSON)
    public String deleteRegionArea(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            Object areaObj = paramMap.get("areaId");
            if(projectObj==null||areaObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long areaId=Long.parseLong(areaObj.toString());
                Boolean flag=regionAreaService.deleteArea(projectId,areaId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/delete/area"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 所有建筑楼信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "buildingList",produces = CommonUtils.MediaTypeJSON)
    public String buildingList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EntityDto> buildingInfoList=regionBuildingService.getBuildingfInfoList(projectId);

                if(CollectionUtils.isEmpty(buildingInfoList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(buildingInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/buildingList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/save/building",produces = CommonUtils.MediaTypeJSON)
    public String saveBuilding(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object buildingNameObj = paramMap.get("buildingName");
            Object areaIdObj = paramMap.get("areaId");
            Object projectIdObj = paramMap.get("projectId");
            if(buildingNameObj==null||areaIdObj==null||projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                String buildingName =buildingNameObj.toString() ;
                String areaIdStr=areaIdObj.toString();
                Building building =regionBuildingService.findByNameProject(buildingName,projectId);
                if(building!=null){
                    result.setMsg("建筑楼名称信息已存在，请修改后重试");
                }else{
                    Building  newBuilding= new Building();
                    Date date=new Date();
                    newBuilding.setName(buildingName);
                    newBuilding.setProjectId(projectId);
                    newBuilding.setCreateTime(date);
                    newBuilding.setUpdateTime(date);
                    if(!areaIdStr.equals("0")){
                        newBuilding.setAreaId(Long.parseLong(areaIdStr));
                    }else{
                        newBuilding.setAreaId(null);
                    }
                    regionBuildingService.insert(newBuilding);
                    result.setCode(1);
                    result.setData(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/save/building"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/update/building",produces = CommonUtils.MediaTypeJSON)
    public String updateBuilding(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object buildingIdObj = paramMap.get("buildingId");
            Object projectIdObj = paramMap.get("projectId");
            Object buildingNameObj = paramMap.get("buildingName");
            if(buildingIdObj==null||buildingNameObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long buildingId= Long.parseLong(buildingIdObj.toString());
                Long projectId= Long.parseLong(projectIdObj.toString());
                String buildingName= buildingNameObj.toString();
                Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                if(buildingName.equals(building.getName())){
                    result.setMsg("建筑楼名称信息未改变,请修改名称后重试");
                }else{
                    Building byNameProject = regionBuildingService.findByNameProject(buildingName, projectId);
                    if(byNameProject!=null){
                        result.setMsg("建筑楼名称信息已存在，请修改名称后重试");
                    }else{
                        Building updateBuilding = new Building();
                        Date date=new Date();
                        updateBuilding.setUpdateTime(date);
                        updateBuilding.setId(buildingId);
                        updateBuilding.setName(buildingName);
                        regionBuildingService.update(updateBuilding);
                        result.setCode(1);
                        result.setData(1);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/update/building"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/delete/building",produces = CommonUtils.MediaTypeJSON)
    public String deleteBuilding(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object buildingIdObj = paramMap.get("buildingId");
            if(projectIdObj==null||buildingIdObj==null){
                result=  Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long buildingId=Long.parseLong(buildingIdObj.toString());
                Boolean flag= regionBuildingService.deleteByBuildingId(projectId,buildingId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/delete/building"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "storeyList",produces = CommonUtils.MediaTypeJSON)
    public String storeyList(@RequestBody Map<String,Object> paramMap ){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EntityDto> storeyList=regionStoreyService.getStoreyInfoList(projectId);
                if(CollectionUtils.isEmpty(storeyList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(storeyList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/storeyList"));
            result=Result.exceptionRe(result);

        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/save/storey",produces = CommonUtils.MediaTypeJSON)
    public String saveStorey(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object areaIdObj = paramMap.get("areaId");
            Object buildingIdObj = paramMap.get("buildingId");
            Object storeyNameObj = paramMap.get("storeyName");
            if(projectIdObj==null||areaIdObj==null||buildingIdObj==null||storeyNameObj==null){
                result=  Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                String areaIdStr= areaIdObj.toString();
                Long buildingId= Long.parseLong(buildingIdObj.toString());
                String storeyName= storeyNameObj.toString();
                Storey newStorey = new Storey();
                Date date=new Date();
                newStorey.setCreateTime(date);
                newStorey.setUpdateTime(date);
                newStorey.setBuildingId(buildingId);
                newStorey.setName(storeyName);
                newStorey.setProjectId(projectId);
                if(!areaIdStr.equals("0")){
                    newStorey.setAreaId(Long.parseLong(areaIdStr));
                }
                regionStoreyService.insert(newStorey);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/save/storey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/update/storey",produces = CommonUtils.MediaTypeJSON)
    public String updateStorey(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object storeyIdObj = paramMap.get("storeyId");
            Object storeyNameObj = paramMap.get("storeyName");
            if(storeyIdObj==null||storeyNameObj==null){
                result= Result.paramError(result);
            }else{
                Long storeyId=Long.parseLong(storeyIdObj.toString());
                String storeyName=storeyNameObj.toString();
                Storey storey = regionStoreyService.selectByPrimaryKey(storeyId);
                if(storeyName.equals(storey.getName())){
                    result.setMsg("层名称信息为改变，请修改后重试");
                }else{
                    Date date = new Date();
                    Storey updateStorey = new Storey();
                    updateStorey.setId(storeyId);
                    updateStorey.setName(storeyName);
                    updateStorey.setUpdateTime(date);
                    regionStoreyService.update(updateStorey);
                    result.setData(1);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/update/storey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/delete/storey",produces = CommonUtils.MediaTypeJSON)
    public String deleteStorey(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object storeyIdObj = paramMap.get("storeyId");
            Object projectIdObj = paramMap.get("projectId");
            if(storeyIdObj==null||projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long storeyId=Long.parseLong(storeyIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Boolean flag=regionStoreyService.deleteByStoreyId(storeyId,projectId);
                if(flag){
                    result.setData(1);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/delete/storey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 房间列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "roomList",produces = CommonUtils.MediaTypeJSON)
    public String roomList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EntityDto> roomList=regionRoomService.selectInfoByProjectId(projectId);
                if(CollectionUtils.isEmpty(roomList)){
                    result=Result.empty(result);
                }else{
                    result.setData(roomList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/roomList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 添加房间信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save/room",produces = CommonUtils.MediaTypeJSON)
    public String saveRoom(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object areaIdObj = paramMap.get("areaId");
            Object buildingIdObj = paramMap.get("buildingId");
            Object storeyIdObj = paramMap.get("storeyId");
            Object roomNameObj = paramMap.get("roomName");
            if(projectIdObj==null||areaIdObj==null||buildingIdObj==null||storeyIdObj==null||roomNameObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                String areaIdStr= areaIdObj.toString();
                Long buildingId= Long.parseLong(buildingIdObj.toString());
                Long storeyId= Long.parseLong(storeyIdObj.toString());
                String roomName=roomNameObj.toString();
                Room newRoom= new Room();
                newRoom.setBuildingId(buildingId);
                newRoom.setStoreyId(storeyId);
                Date date = new Date();
                newRoom.setCreateTime(date);
                newRoom.setUpdateTime(date);
                newRoom.setProjectId(projectId);
                newRoom.setName(roomName);
                if(!areaIdStr.equals("0")){
                    newRoom.setAreaId(Long.parseLong(areaIdStr));
                }
                regionRoomService.insert(newRoom);
                result.setData(1);
                result.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/save/room"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/update/room",produces = CommonUtils.MediaTypeJSON)
    public String updateRoom(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object roomIdObj = paramMap.get("roomId");
            Object roomNameObj = paramMap.get("roomName");
            if(roomIdObj==null||roomNameObj==null|| StringUtils.isEmpty(roomNameObj.toString())){
                result = Result.paramError(result);
            }else{
                Long roomId=Long.parseLong(roomIdObj.toString());
                String roomName=roomNameObj.toString();
                Room room = regionRoomService.selectByPrimaryKey(roomId);
                if(roomName.equals(room.getName())){
                    result.setMsg("房间名称信息未改变，请修改后重试");
                }else{
                    Date date = new Date();
                    Room updateRoom = new Room();
                    updateRoom.setName(roomName);
                    updateRoom.setId(roomId);
                    updateRoom.setUpdateTime(date);
                    regionRoomService.update(updateRoom);
                    result.setData(1);
                    result.setCode(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/update/room"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除房间信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/delete/room",produces = CommonUtils.MediaTypeJSON)
    public String deleteRoom(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object roomIdObj = paramMap.get("roomId");
            Object projectIdObj = paramMap.get("projectId");
            if(roomIdObj==null||projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long roomId=Long.parseLong(roomIdObj.toString());
                Boolean flag= regionRoomService.deleteByRoomId(projectId,roomId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/manager/delete/room"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
