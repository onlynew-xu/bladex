package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Area;
import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.entity.Room;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

@Api(tags ="区域管理")
@RestController
@RequestMapping("/api/region")
public class RegionController extends BaseController{
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;

    /**
     * 区域列表信息
     * @param paramMap
     * @return
     */
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

                    Boolean flag=regionBuildingService.getAreaFlag(projectId);
                    if(flag){
                        dataList.add(new EntityDto(0l,"未知区域",projectId));
                    }
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/areaList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 建筑列表信息
     * @return
     */
    @PostMapping(value = "buildingList",produces = CommonUtils.MediaTypeJSON)
    public String buildingList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object areaIdObj = paramMap.get("areaId");
            if(projectIdObj==null||areaIdObj==null){
                 result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long areaId=Long.parseLong(areaIdObj.toString());
                List<Building> buildingList=null;
                if(areaId.equals(0L)){
                    buildingList=regionBuildingService.selectByProjectIdAndAreaId(projectId,null);
                }else{
                    buildingList=regionBuildingService.selectByProjectIdAndAreaId(projectId,areaId);
                }
                if(CollectionUtils.isEmpty(buildingList)){
                    result=Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Building building : buildingList) {
                        dataList.add(new EntityDto(building.getId(),building.getName(),building.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/buildingList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 楼层列表信息
     * @return
     */
    @PostMapping(value = "storeyList",produces = CommonUtils.MediaTypeJSON)
    public String storeyList(@RequestBody Map<String,Object> paramMap ){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object buildingIdObj = paramMap.get("buildingId");
            if(projectIdObj==null||buildingIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long buildingId=Long.parseLong(buildingIdObj.toString());
                List<Storey> storeyList=regionStoreyService.getByProjectAndBuildind(projectId,buildingId);
                if(CollectionUtils.isEmpty(storeyList)){
                   result= Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Storey storey : storeyList) {
                        dataList.add(new EntityDto(storey.getId(),storey.getName(),storey.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/storeyList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
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
            Object storeyIdObj = paramMap.get("storeyId");
            if(projectIdObj==null||storeyIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long storeyId=Long.parseLong(storeyIdObj.toString());
                List<Room> roomList=regionRoomService.selectByProjectIdAndStoreyId(projectId,storeyId);
                if(CollectionUtils.isEmpty(roomList)){
                   result=Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Room room : roomList) {
                        dataList.add(new EntityDto(room.getId(),room.getName(),room.getProjectId()));

                    }
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/roomList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 添加区域信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/saveRegionArea",produces = CommonUtils.MediaTypeJSON)
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/saveRegionArea"));
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
    @PostMapping(value = "/updateRegionArea",produces = CommonUtils.MediaTypeJSON)
    public String updateRegionArea(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object areaIdObj = paramMap.get("areaId");
            Object areaNameObj = paramMap.get("areaName");
            if(areaIdObj==null||areaNameObj==null){
               result= Result.paramError(result);
            }else{
                String areaName=areaNameObj.toString();
                Long areaId=Long.parseLong(areaIdObj.toString());
                Date date = new Date();
                Area updateArea = new Area();
                updateArea.setName(areaName);
                updateArea.setId(areaId);
                updateArea.setUpdateTime(date);
                regionAreaService.update(updateArea);
                result.setData(1);
                result.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/updateRegionArea"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 删除区域信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/deleteRegionArea",produces = CommonUtils.MediaTypeJSON)
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
                Boolean flag=regionAreaService.removeByProjectAndAreaId(projectId,areaId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/deleteRegionArea"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 添加建筑信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/saveBuilding",produces = CommonUtils.MediaTypeJSON)
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
                Building  newBuilding= new Building();
                Date date=new Date();
                newBuilding.setName(buildingName);
                newBuilding.setProjectId(projectId);
                newBuilding.setCreateTime(date);
                newBuilding.setUpdateTime(date);
                if(!areaIdStr.equals("0")){
                    newBuilding.setAreaId(Long.parseLong(areaIdStr));
                }
                regionBuildingService.insert(newBuilding);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/saveBuilding"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 修改建筑名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/updateBuilding",produces = CommonUtils.MediaTypeJSON)
    public String updateBuilding(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object buildingIdObj = paramMap.get("buildingId");
            Object buildingNameObj = paramMap.get("buildingName");
            if(buildingIdObj==null||buildingNameObj==null){
                result=Result.paramError(result);
            }else{
                Long buildingId= Long.parseLong(buildingIdObj.toString());
                String buildingName= buildingNameObj.toString();
                Building updateBuilding = new Building();
                Date date=new Date();
                updateBuilding.setUpdateTime(date);
                updateBuilding.setId(buildingId);
                updateBuilding.setName(buildingName);
                regionBuildingService.update(updateBuilding);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/updateBuilding"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 删除建筑信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/deleteBuilding",produces = CommonUtils.MediaTypeJSON)
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/deleteBuilding"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 添加楼层信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/saveStorey",produces = CommonUtils.MediaTypeJSON)
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/saveStorey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 修改楼层名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/updateStorey",produces = CommonUtils.MediaTypeJSON)
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
                Date date = new Date();
                Storey updateStorey = new Storey();
                updateStorey.setId(storeyId);
                updateStorey.setName(storeyName);
                updateStorey.setUpdateTime(date);
                regionStoreyService.update(updateStorey);
                result.setData(1);
                result.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/updateStorey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除楼层信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/deleteStorey",produces = CommonUtils.MediaTypeJSON)
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/deleteStorey"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 添加房间信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/saveRooom",produces = CommonUtils.MediaTypeJSON)
    public String saveRooom(@RequestBody Map<String,Object> paramMap){
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/saveRooom"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改房间信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/updateRoom",produces = CommonUtils.MediaTypeJSON)
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
                Date date = new Date();
                Room updateRoom = new Room();
                updateRoom.setName(roomName);
                updateRoom.setId(roomId);
                updateRoom.setUpdateTime(date);
                regionRoomService.update(updateRoom);
                result.setData(1);
                result.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/updateRoom"));
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
    @PostMapping(value = "/deleteRoom",produces = CommonUtils.MediaTypeJSON)
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
            logger.error(ExceptionUtils.getStackMsg(e,"/api/region/deleteRoom"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
         return JsonUtils.toJson(result,type);
    }
}
