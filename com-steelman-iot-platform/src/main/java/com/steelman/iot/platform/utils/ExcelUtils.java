package com.steelman.iot.platform.utils;

import com.alibaba.druid.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/12 0012 11:14
 * @Description:
 */
public class ExcelUtils {

    public static <T extends Map<String, List<V>>, V> XSSFWorkbook exportFile(T t, Class<V> classOfV) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Map.Entry<String, List<V>> entry : t.entrySet()) {
            XSSFSheet sheet = workbook.createSheet(entry.getKey());
            int rowCount = 0;
            for (V v : entry.getValue()) {
                int column = 0;
                XSSFRow row = sheet.createRow(rowCount);
                Map<String, String> content = mergeObject(classOfV, v, true);
                for (Map.Entry<String, String> item : content.entrySet()) {
                    XSSFCell cell = row.createCell(column);
                    cell.setCellValue(item.getValue());
                    column++;
                }
                rowCount++;
            }
        }
        return workbook;

    }


    public static XSSFWorkbook export(String sheetName, List<String> titleList, List<Map<String, String>> dataList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //title
        XSSFRow row = sheet.createRow(0);
        int column = 0;
        for (String title : titleList) {
            XSSFCell xssfCell = row.createCell(column);
            xssfCell.setCellValue(title);
            column++;
        }
        int rowCount = 1;
        for (Map<String, String> map : dataList) {
            int columnCount = 0;
            XSSFRow dataRow = sheet.createRow(rowCount);
            for (String key : map.keySet()) {
                XSSFCell cell = dataRow.createCell(columnCount);
                cell.setCellValue(map.get(key));
                columnCount++;
            }
            rowCount++;
        }
        return workbook;
    }
    public static XSSFWorkbook exportDate(List<String> titleList, Map<String,String[][]> dataMap) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        CellStyle cellStyleTitle=workbook.createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);




        if(!CollectionUtils.isEmpty(dataMap)) {
            for (Map.Entry<String, String[][]> stringEntry : dataMap.entrySet()) {
                String key = stringEntry.getKey();
                String[][] value = stringEntry.getValue();
                XSSFSheet sheet = workbook.createSheet(key);
                XSSFRow row = sheet.createRow(0);
                sheet.createFreezePane(0,1,0,1);
                sheet.setColumnWidth(0,40*256);
                int column = 0;
                for (String title : titleList) {
                    XSSFCell xssfCell = row.createCell(column);
                    xssfCell.setCellStyle(cellStyleTitle);
                    xssfCell.setCellValue(title);
                    column++;
                }
                for (int i = 0; i < value.length; i++) {
                    XSSFRow rowData = sheet.createRow(i + 1);
                    sheet.setColumnWidth(i + 1,40*256);
                    for (int j = 0; j < value[i].length; j++) {
                        XSSFCell cell = rowData.createCell(j);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(value[i][j]);
                    }
                }
            }
        }
        return workbook;
    }


    public static <T> Map<String, String> mergeObject(Class<T> classOfT, T t, boolean blank) throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        Field[] fields = classOfT.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[i].getName(), classOfT);
            Method getMethod = propertyDescriptor.getReadMethod();
            Object o = getMethod.invoke(t);
            if (!blank && (o == null || StringUtils.isEmpty(o.toString()) || "null".equals(o.toString())))
                continue;
            map.put(fields[i].getName().trim(), o == null ? "" : o.toString());
        }

        return map;
    }
}
