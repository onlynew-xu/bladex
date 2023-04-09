package com.steelman.iot.platform.controller;

import com.github.pagehelper.PageHelper;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.utils.RequestUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author tang
 * date 2020-11-23
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Type ParamMapType = new TypeToken<Map<String, String>>() {
    }.getType();

    protected String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null) {
            return null;
        }
        try {
            value = new String(value.getBytes("iso-8859-1"), "UTF-8");
            return value;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);//(ex.getMessage(), ex);
        }

        return null;
    }

    protected String getRequestEntity(HttpServletRequest request) throws IOException {
        return RequestUtils.getRequestEntity(request);

    }

    public void logError(Exception ex, Long projectId) {
        if (projectId != null && projectId != 0l) {
            String msg = "[projectId:" + projectId + "]";
            logger.error(msg + ex.getMessage(), ex);
        } else {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void logError(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }

    public <T> void setDataAndTimeStamp(Result<T> result, T res) {
        result.setData(res);
        result.setTimestamp(new Date());
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        int page = 0;
        if (!StringUtils.isEmpty(RequestUtils.getParameter("page"))) {
            page = (Integer.valueOf(RequestUtils.getParameter("page")));
        }
        int size = 0;
        if (!StringUtils.isEmpty(RequestUtils.getParameter("size"))) {
            size = (Integer.valueOf(RequestUtils.getParameter("size")));
        }
        if (page > 0 && size > 0) {
            PageHelper.startPage(page, size);
        }
    }
}
