package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.utils.IdGeneratorWorker;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected IdGeneratorWorker idGeneratorWorker;

    /**
     * 生成Id
     * @return
     */
    protected long generateId(){
        return idGeneratorWorker.nextId();
    }

    /**
     * 记录异常
     * @param ex
     * @param projectId
     */
    public void logError(Exception ex, long projectId,Long deviceId){
        String msg = "[projectId=" + projectId + ","+"deviceId="+deviceId+"]";
        logger.error(msg + ex.getLocalizedMessage(), ex);
    }

    public Pager<T> pagerText(Map<String, Integer> params, List<T> list) {
        int page = params.get("page");
        int size = params.get("size");
        Pager<T> pager = new Pager<T>();
        pager.setPage(page);
        pager.setSize(size);
        pager.setRows(list);
        pager.setTotal(list.size());
        return pager;
    }
}
