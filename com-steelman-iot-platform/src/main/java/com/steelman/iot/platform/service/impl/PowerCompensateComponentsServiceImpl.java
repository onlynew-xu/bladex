package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerCompensateComponentsDao;
import com.steelman.iot.platform.entity.PowerCompensateComponents;
import com.steelman.iot.platform.service.PowerCompensateComponentsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/15 0015 15:55
 * @Description:
 */
@Service("powerCompensateComponentsService")
public class PowerCompensateComponentsServiceImpl extends BaseService implements PowerCompensateComponentsService {

    @Resource
    private PowerCompensateComponentsDao componentsDao;

    @Override
    public void insert(PowerCompensateComponents compensate) {
        componentsDao.insert(compensate);
    }

    @Override
    public void update(PowerCompensateComponents components) {
        componentsDao.updateByPrimaryKeySelective(components);
    }

    @Override
    public void delete(Long id) {
        componentsDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<PowerCompensateComponents> getComponentsList(Long compensateId) {
        return componentsDao.selectByCompensateId(compensateId);
    }

    @Override
    public PowerCompensateComponents selectById(Long compensateComponentsId) {
        return componentsDao.selectByPrimaryKey(compensateComponentsId);
    }

    @Override
    public Boolean updateInfo(Map<String, Object> paramMap) {
        PowerCompensateComponents powerCompensateComponents=new PowerCompensateComponents();
        Object id = paramMap.get("id");
        powerCompensateComponents.setId(Long.parseLong(id.toString()));
        Boolean flag=false;
        Object componentsName = paramMap.get("name");
        Object componentsBrand = paramMap.get("brand");
        Object effectiveDate = paramMap.get("effectiveDate");
        Object compensateCap = paramMap.get("compensateCap");
        if(componentsName!=null){
            powerCompensateComponents.setName(componentsName.toString());
            flag=true;
        }
        if(componentsBrand!=null){
            powerCompensateComponents.setBrand(componentsBrand.toString());
            flag=true;
        }
        if(effectiveDate!=null){
            powerCompensateComponents.setEffectiveDate(effectiveDate.toString());
            flag=true;
        }
        if(compensateCap!=null){
            powerCompensateComponents.setCompensateCap(compensateCap.toString());
            flag=true;
        }
        if(flag){
            componentsDao.updateByPrimaryKeySelective(powerCompensateComponents);
        }
        return true;
    }
}
