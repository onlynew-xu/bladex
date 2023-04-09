package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.DevicePictureService;
import com.steelman.iot.platform.service.ProjectService;
import com.steelman.iot.platform.service.RegionPictureService;
import com.steelman.iot.platform.service.RegionStoreyService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "图片管理")
@RequestMapping("/api/picture")
@Slf4j
@RestController
public class PictureController extends BaseController {
    @Resource
    private DevicePictureService pictureService;
    @Resource
    private RegionPictureService floorPictureService;
    @Resource
    private ProjectService projectService;
    @Value("${iot.image.url}")
    private String imgUrl;
    @Value("${fileUpload}")
    private String fileLoad;
    @Resource
    private RegionStoreyService regionStoreyService;


    /**
     * 项目信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/project",produces = CommonUtils.MediaTypeJSON)
    public String getProject(@RequestBody Map<String,Object> paramMap){
        Result<Project> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                Project byId = projectService.findById(projectId);
                if(byId!=null){
                    result.setCode(1);
                    result.setData(byId);
                }else{
                    Result.paramError(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/project"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Project>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 项目上传bim图
     * @param id
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/project/bim",produces = CommonUtils.MediaTypeJSON)
    public String uploadProjectBim(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Project project = projectService.findById(id);
            String originalFilename = file.getOriginalFilename();
            //获取文件名后缀
            int index = originalFilename.lastIndexOf(".");
            String substring = originalFilename.substring(index);
            //处理真正的文件名 层Id+"ping"+文件后缀
            String fileChild=id+"bim"+substring;
            String canonicalPath=null;
            if(fileLoad.equals("local")){
                //本地测试环境
                File picFile=new File("");
                canonicalPath = picFile.getCanonicalPath()+"/images/project/";
            }else{
                //先上环境
                canonicalPath="/steelman/nginx/images/project/";
            }
            //拼接文件全名
            File fileName= new File(canonicalPath+id,fileChild);
            if (!fileName.getParentFile().exists()) {
                //创建多层文件夹
                fileName.getParentFile().mkdirs();
            }
            file.transferTo(fileName);
            Project project1=new Project();
            project1.setId(id);
            project1.setUpdateTime(new Date());
            String bimUrl=imgUrl+"/project/"+id+"/"+fileChild;
            project1.setBim(bimUrl);
            projectService.update(project1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/project/bim"));
            result=Result.exceptionRe(result);
        }
        result.setCode(1);
        result.setData(1);
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 项目重新上传bim图
     * @param id
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/project/bim/reload",produces = CommonUtils.MediaTypeJSON)
    public String reloadProjectBim(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            String originalFilename = file.getOriginalFilename();
            //获取文件名后缀
            int index = originalFilename.lastIndexOf(".");
            String substring = originalFilename.substring(index);
            //处理真正的文件名 层Id+"ping"+文件后缀
            String fileChild=id+"bim"+substring;
            String canonicalPath=null;
            if(fileLoad.equals("local")){
                //本地测试环境
                File picFile=new File("");
                canonicalPath = picFile.getCanonicalPath()+"/images/project/";
            }else{
                //先上环境
                canonicalPath="/steelman/nginx/images/project/";
            }
            //拼接文件全名
            File fileName= new File(canonicalPath+id,fileChild);
            if (!fileName.getParentFile().exists()) {
                //创建多层文件夹
                fileName.getParentFile().mkdirs();
            }
            file.transferTo(fileName);
            Project project1=new Project();
            project1.setId(id);
            project1.setUpdateTime(new Date());
            String bimUrl=imgUrl+"/project/"+id+"/"+fileChild;
            project1.setBim(bimUrl);
            projectService.update(project1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/project/bim/reload"));
            result=Result.exceptionRe(result);
        }
        result.setCode(1);
        result.setData(1);
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 楼层信息列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/storey/list",produces = CommonUtils.MediaTypeJSON)
    public String storeyList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EntityDto> storeyList=regionStoreyService.getStoreyInfoList(projectId);
                if(!CollectionUtils.isEmpty(storeyList)){
                   result.setCode(1);
                   result.setData(storeyList);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 楼层详细信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/storey/detail",produces = CommonUtils.MediaTypeJSON)
    public String storeyDetail(@RequestBody Map<String,Object> paramMap){
        Result<Storey> result=new Result<>();
        result.setCode(0);
        try {
            Object storeyIdObj = paramMap.get("storeyId");
            if(storeyIdObj==null){
                result= Result.paramError(result);
            }else{
                Long storeyId=Long.parseLong(storeyIdObj.toString());
                Storey storey=regionStoreyService.selectByPrimaryKey(storeyId);
                if(storey!=null){
                    result.setCode(1);
                    result.setData(storey);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Storey>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/storey/picture",produces = CommonUtils.MediaTypeJSON)
    public String uploadStoreyPicture(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            if(id==null||file==null){
                result= Result.paramError(result);
            }else{
                Storey storey = regionStoreyService.selectByPrimaryKey(id);
                Long projectId = storey.getProjectId();
                String originalFilename = file.getOriginalFilename();
                //获取文件名后缀
                int index = originalFilename.lastIndexOf(".");
                String substring = originalFilename.substring(index);
                //处理真正的文件名 层Id+"ping"+文件后缀
                String fileChild=id+substring;
                String canonicalPath=null;
                if(fileLoad.equals("local")){
                    //本地测试环境
                    File picFile=new File("");
                    canonicalPath = picFile.getCanonicalPath()+"/images/project/";
                }else{
                    //先上环境
                    canonicalPath="/steelman/nginx/images/project/";
                }
                //拼接文件全名
                File fileName= new File(canonicalPath+projectId+"/storey/",fileChild);
                //判断父文件夹是否存在
                if (!fileName.getParentFile().exists()) {
                    //创建多层文件夹
                    fileName.getParentFile().mkdirs();
                }
                file.transferTo(fileName);
                Date date = new Date();
                Storey storey1=new Storey();
                storey1.setId(storey.getId());
                storey1.setPicture(imgUrl+"/project/"+projectId+"/storey/"+fileChild);
                storey1.setUpdateTime(date);
                regionStoreyService.update(storey1);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/picture"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/storey/picture/reload",produces = CommonUtils.MediaTypeJSON)
    public String reloadStoreyPicture(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            if(id==null||file==null){
                result= Result.paramError(result);
            }else{
                Storey storey = regionStoreyService.selectByPrimaryKey(id);
                String originalFilename = file.getOriginalFilename();
                //获取文件名后缀
                int index = originalFilename.lastIndexOf(".");
                String substring = originalFilename.substring(index);
                //处理真正的文件名 层Id+"ping"+文件后缀
                String fileChild=id+substring;
                String canonicalPath=null;
                if(fileLoad.equals("local")){
                    //本地测试环境
                    File picFile=new File("");
                    canonicalPath = picFile.getCanonicalPath()+"/images/project/";
                }else{
                    //先上环境
                    canonicalPath="/steelman/nginx/images/project/";
                }
                //拼接文件全名
                File fileName= new File(canonicalPath+storey.getProjectId()+"/storey/",fileChild);
                //判断父文件夹是否存在
                if (!fileName.getParentFile().exists()) {
                    //创建多层文件夹
                    fileName.getParentFile().mkdirs();
                }
                file.transferTo(fileName);
                Date date = new Date();
                Storey storey1=new Storey();
                storey1.setId(storey.getId());
                storey1.setPicture(imgUrl+"/project/"+storey.getProjectId()+"/storey/"+fileChild);
                storey1.setUpdateTime(date);
                regionStoreyService.update(storey1);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/picture/reload"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/storey/bim",produces = CommonUtils.MediaTypeJSON)
    public String uploadStoreyBim(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            if(id==null||file==null){
                result= Result.paramError(result);
            }else{
                Storey storey = regionStoreyService.selectByPrimaryKey(id);
                Long projectId=storey.getProjectId();
                String originalFilename = file.getOriginalFilename();
                //获取文件名后缀
                int index = originalFilename.lastIndexOf(".");
                String substring = originalFilename.substring(index);
                //处理真正的文件名 层Id+"ping"+文件后缀
                String fileChild=id+"bim"+substring;
                String canonicalPath=null;
                if(fileLoad.equals("local")){
                    //本地测试环境
                    File picFile=new File("");
                    canonicalPath = picFile.getCanonicalPath()+"/images/project/";
                }else{
                    //先上环境
                    canonicalPath="/steelman/nginx/images/project/";
                }
                //拼接文件全名
                File fileName= new File(canonicalPath+projectId+"/storey/",fileChild);
                //判断父文件夹是否存在
                if (!fileName.getParentFile().exists()) {
                    //创建多层文件夹
                    fileName.getParentFile().mkdirs();
                }
                file.transferTo(fileName);
                Storey storey1=new Storey();
                Date date=new Date();
                storey1.setId(storey.getId());
                storey1.setCadpicture(imgUrl+"/project/"+projectId+"/storey/"+fileChild);
                storey1.setUpdateTime(date);
                regionStoreyService.update(storey1);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/bim"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/storey/bim/reload",produces = CommonUtils.MediaTypeJSON)
    public String reloadStoreyBim(@RequestParam("id") Long id,@RequestParam("file") MultipartFile file) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            if(id==null||file==null){
                result= Result.paramError(result);
            }else{
                Storey storey = regionStoreyService.selectByPrimaryKey(id);
                Long projectId=storey.getProjectId();
                String originalFilename = file.getOriginalFilename();
                //获取文件名后缀
                int index = originalFilename.lastIndexOf(".");
                String substring = originalFilename.substring(index);
                //处理真正的文件名 层Id+"ping"+文件后缀
                String fileChild=id+"bim"+substring;
                String canonicalPath=null;
                if(fileLoad.equals("local")){
                    //本地测试环境
                    File picFile=new File("");
                    canonicalPath = picFile.getCanonicalPath()+"/images/project/";
                }else{
                    //先上环境
                    canonicalPath="/steelman/nginx/images/project/";
                }
                //拼接文件全名
                File fileName= new File(canonicalPath+projectId+"/storey/",fileChild);
                //判断父文件夹是否存在
                if (!fileName.getParentFile().exists()) {
                    //创建多层文件夹
                    fileName.getParentFile().mkdirs();
                }
                file.transferTo(fileName);
                Storey storey1=new Storey();
                Date date=new Date();
                storey1.setId(storey.getId());
                storey1.setCadpicture(imgUrl+"/project/"+projectId+"/storey/"+fileChild);
                storey1.setUpdateTime(date);
                regionStoreyService.update(storey1);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/picture/storey/bim/reload"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



}
