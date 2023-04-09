package com.steelman.iot.platform.utils;




import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RequestUtils {
    /**
     * 从Get参数获取整数值
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static int getQueryInt(HttpServletRequest request, String paramName, int defaultValue)
    {
        int val = defaultValue;
        if(StringUtils.isEmpty(paramName))
        {
            return  val;
        }
        String paramVal = request.getParameter(paramName);
        if(!StringUtils.isEmpty(paramVal) && StringUtils.isNumeric(paramVal))
        {
            val = Integer.parseInt(paramVal);
        }
        return  val;
    }

    /**
     * 从Get参数中获取长整型值
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static long getQueryLong(HttpServletRequest request, String paramName, long defaultValue)
    {
        long val = defaultValue;
        if(StringUtils.isEmpty(paramName))
        {
            return  val;
        }
        String paramVal = request.getParameter(paramName);
        if(!StringUtils.isBlank(paramVal) && StringUtils.isNumeric(paramVal))
        {
            val = Long.parseLong(paramVal);
        }
        return  val;
    }

    public static String getQueryString(HttpServletRequest request, String paramName, String defaultValue)
    {
        String val = defaultValue;
        if(StringUtils.isEmpty(paramName))
        {
            return  val;
        }
        val = request.getParameter(paramName);
        return  val;
    }

    /**
     * 获取RequestEntity
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestEntity(HttpServletRequest request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = bis.read();
        while(read != -1){
            baos.write(read);
            read = bis.read();
        }
        bis.close();
        byte data[] = baos.toByteArray();

        return new String(data, "utf-8");
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getAttributes().getRequest();
    }

}
