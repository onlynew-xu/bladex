package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.LogOperatorDao;
import com.steelman.iot.platform.entity.LogOperator;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.dto.LogOperatorDto;
import com.steelman.iot.platform.service.LogOperatorService;
import com.steelman.iot.platform.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tang
 * date 2020-11-23
 */
@Service("logOperatorService")
public class LogOperatorServiceImpl extends BaseService implements LogOperatorService {
    @Resource
    private LogOperatorDao logOperatorDao;
    @Resource
    private ProjectService projectService;
    @Override
    public Integer saveLog(LogOperator logOperator) {
        return logOperatorDao.insert(logOperator);
    }

    @Override
    public List<LogOperator> getLogOperate(Long userId) {

        return logOperatorDao.selectByUserId(userId);
    }

    @Override
    public List<LogOperatorDto> getLogOperateByProjectId(Long projectId) {
       List<LogOperator> operatorList= logOperatorDao.getLogOperateByProjectId(projectId);
       Project project = projectService.findById(projectId);
       List<LogOperatorDto> logOperatorDtoList=new ArrayList<>();
       if(!CollectionUtils.isEmpty(operatorList)){
           for (LogOperator logOperator : operatorList) {
               logOperatorDtoList.add(new LogOperatorDto(logOperator.getUsername(),logOperator.getOperation(),logOperator.getCreateTime(),project.getId(),project.getName()));
           }
       }
       if(!CollectionUtils.isEmpty(logOperatorDtoList)){
           return logOperatorDtoList;
       }
       return null;
    }
}
