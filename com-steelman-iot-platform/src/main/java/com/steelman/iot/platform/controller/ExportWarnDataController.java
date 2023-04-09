package com.steelman.iot.platform.controller;

import com.steelman.iot.platform.entity.AlarmItem;
import com.steelman.iot.platform.entity.AlarmWarn;
import com.steelman.iot.platform.service.AlarmItemService;
import com.steelman.iot.platform.service.AlarmWarnService;
import com.steelman.iot.platform.service.DeviceMeasurementService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/export")
public class ExportWarnDataController {
   @Resource
   private AlarmWarnService alarmWarnService;
   @Resource
   private DeviceMeasurementService deviceMeasurementService;
   @Resource
   private AlarmItemService alarmItemService;
    @RequestMapping(value = "export",method = RequestMethod.GET)
    public void exportAlarmWarnData(@RequestParam("id") Long deviceId, HttpServletResponse response) throws IOException {
       List<AlarmWarn> alarmWarnList=alarmWarnService.getByDeviceId(deviceId,1000L,"2021-05-03 9:00:00",new Date());
       List<AlarmItem> alarmItemList=alarmItemService.selectAll();
       Map<Long,String>  alarmItemMap=new HashMap<>();
        for (AlarmItem alarmItem : alarmItemList) {
            alarmItemMap.put(alarmItem.getId(),alarmItem.getName());
        }
       XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("设备告警");
        //title
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("设备Sn");
        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("告警项目");
        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("告警值");
        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("告警时间");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       if(!CollectionUtils.isEmpty(alarmWarnList)){
            int column=1;
            for (AlarmWarn alarmWarn : alarmWarnList) {
                XSSFRow rowl = sheet.createRow(column);
                XSSFCell cell4 = rowl.createCell(0);
                cell4.setCellValue(alarmWarn.getSerialNum());
                XSSFCell cell5 = rowl.createCell(1);
                cell5.setCellValue(alarmItemMap.get(alarmWarn.getAlarmItemId()));
                XSSFCell cell6 = rowl.createCell(2);
                cell6.setCellValue(alarmWarn.getAlarmValue());
                XSSFCell cell7 = rowl.createCell(3);
                cell7.setCellValue(simpleDateFormat.format(alarmWarn.getCreateTime()));
                column++;
            }
       }
        String fileName="设备预警详情.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "filename");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);//默认Excel名称
        response.setHeader("filename", fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
}
