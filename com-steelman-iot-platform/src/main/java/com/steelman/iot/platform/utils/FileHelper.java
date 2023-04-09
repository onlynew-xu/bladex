package com.steelman.iot.platform.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    private static String[] separator = new String[]{"/", "\\"};

    private static FileHelper fileHelper = new FileHelper();

    /**
     * 创建临时内存文件目录
     * @param timetableIdAndGradeId
     * @return
     */
    public static String getDir(String timetableIdAndGradeId) {
        String path = fileHelper.getClass().getClassLoader().getResource("").getPath();
        path = Paths.get(trimStart(path, separator[0])).getParent().getParent().toString();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String subDir = df.format(new Date());
        path = Paths.get(path, "download",subDir, timetableIdAndGradeId).toString();
        String result = createPath(path.replace(separator[1], separator[0]), separator[0]);
        String downloadpath="download"+ File.separator+subDir+File.separator;
        return downloadpath;
    }

    public static String trimStart(String str1, String str2) {
        if (str1.startsWith(str2) && File.separator.equals("\\")) {
            return str1.substring(str2.length(), str1.length() - str2.length());
        }
        return str1;
    }

    /**
     * 解决不同浏览器的中文乱码问题
     * @param fileName
     * @param request
     * @return
     */
    public static String getEncodeName(String fileName, HttpServletRequest request) {
        try {
            String browserInfo = request.getHeader("User-Agent").toUpperCase();
            if (browserInfo.indexOf("FIREFOX") > 0) {
                return fileName;
            } else if (browserInfo.indexOf("SAFARI") > 0) {
                return fileName;
            } else if (browserInfo.indexOf("CHROME") > 0) {
                return URLEncoder.encode(fileName, "UTF-8");
            } else {
                return URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (java.lang.Exception ex) {
            return fileName;
        }
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String downLoadPath, String contentType, String realName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long fileLength = new File(downLoadPath).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=" + getDownloadName(realName, request));
        response.setHeader("Content-Length", String.valueOf(fileLength));
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        File file=new File(downLoadPath);
        file.delete();
        bis.close();
        bos.close();
    }

    /**
     * 文件名进行编码
     * @param fileName
     * @param request
     * @return
     */
    public static String getDownloadName(String fileName, HttpServletRequest request) {
        try {
            String ua = request.getHeader("user-agent").toLowerCase();

            String ret;
            if (ua.contains("edge")) {
                ret = URLEncoder.encode(fileName, "UTF-8");
            } else {
                ret = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            return ret;
        } catch (Exception e) {
            return fileName;
        }
    }
    private static String createPath(String str1, String str2) {
        if (!str1.endsWith(str2)) {
            return str1 + str2;
        }
        return str1;
    }
}
