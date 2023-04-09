package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.InspectionDao;
import com.steelman.iot.platform.entity.Inspection;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.InspectionDto;
import com.steelman.iot.platform.entity.vo.InspectionInfo;
import com.steelman.iot.platform.service.InspectionService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/31 0031 16:58
 * @Description:
 */
@Service("inspectionService")
public class InspectionServiceImpl extends BaseService implements InspectionService {
    @Resource
    private InspectionDao inspectionDao;

    @Override
    public void save(Inspection inspection) {
        inspectionDao.insert(inspection);
    }

    @Override
    public void revamp(Inspection inspection) {
        inspectionDao.updateByPrimaryKeySelective(inspection);
    }

    @Override
    public void delete(Long id) {
        inspectionDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<InspectionInfo> getList( Long projectId) {

        return inspectionDao.selectByProjectId(projectId);
    }

    @Override
    public Pager<T> selectByAll(Map<String, Integer> params,Long userId, Long projectId) {
        List<T> list = inspectionDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"),userId,projectId);
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public List<Inspection> getActiveInspectionInfo() {
        return inspectionDao.getActiveInspectionInfo();
    }

    @Override
    public InspectionDto getById(Long inspectionId) {
        return inspectionDao.getDtoById(inspectionId);
    }

    @Override
    public List<InspectionDto> getDtoList(Long projectId) {
        return inspectionDao.getDtoList(projectId);
    }
}
