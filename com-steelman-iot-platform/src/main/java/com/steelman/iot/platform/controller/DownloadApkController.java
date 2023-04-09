package com.steelman.iot.platform.controller;

import com.steelman.iot.platform.dao.DeviceMeasurementDao;
import com.steelman.iot.platform.energyManager.dao.DeviceHourDifferMeasurementDao;
import com.steelman.iot.platform.energyManager.dao.DeviceHourMeasurementDao;
import com.steelman.iot.platform.service.AlarmWarnService;
import com.steelman.iot.platform.utils.ExcelUtils;
import com.steelman.iot.platform.utils.FileHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/down")
public class DownloadApkController {
    @Resource
    private DeviceHourDifferMeasurementDao deviceHourDifferMeasurementDao;
    @Resource
    private DeviceHourMeasurementDao deviceHourMeasurementDao;
    @Resource
    private DeviceMeasurementDao deviceMeasurementDao;
    @Resource
    private AlarmWarnService alarmWarnService;

    @RequestMapping(value = "/apk",method = RequestMethod.GET)
    public String downloadApk(HttpServletResponse response) throws Exception{
        String fileAllName="/steelman/apk/steelman.apk";
        File file = new File(fileAllName);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=steelman.apk");
        FileInputStream fileInputStream=new FileInputStream(file);
        ServletOutputStream os = response.getOutputStream();
        int len = -1;
        byte[] b = new byte[1024];
        while ((len = fileInputStream.read(b)) != -1) {
            os.write(b, 0, len);
            os.flush();
        }
        fileInputStream.close();
        os.close();
        return null;
    }
    @GetMapping(value = "/test")
    public void downloadTest(HttpServletRequest request,HttpServletResponse response){
        try {
            String fileName="测试中文名字.xlsx";
            List<String> title =new ArrayList<>();
            title.add("测试标题");
            Map<String,String[][]> dataMap=new LinkedHashMap<>();
            String[][] dataArr=new String[1][1];
            dataArr[0][0]="测试数据";
            dataMap.put("测试",dataArr);
            String realName= FileHelper.getEncodeName(fileName,request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, dataMap);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName,request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @PostMapping(value = "updateAlarmWarn")
//    public String updateAlarmWarn(){
//        List<AlarmWarn> alarmWarnList=alarmWarnService.getByProjectId(2l);
//        List<AlarmWarn> alarmWarnUpdateList=new ArrayList<>();
//        for (AlarmWarn alarmWarn : alarmWarnList) {
//            String alarmValue = alarmWarn.getAlarmValue();
//            if(StringUtils.isNotBlank(alarmValue)){
//                if(alarmValue.contains("%%")){
//                    String replace = alarmValue.replace("%%", "%");
//                    alarmWarn.setAlarmValue(replace);
//                    alarmWarnUpdateList.add(alarmWarn);
//                }
//            }
//        }
//        Integer count=alarmWarnService.updateAlarmValue(alarmWarnUpdateList);
//        return "ok";
//    }
    
}
