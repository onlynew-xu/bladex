package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerWaveComponentsDao;
import com.steelman.iot.platform.entity.PowerWaveComponents;
import com.steelman.iot.platform.service.PowerWaveComponentsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 15:49
 * @Description:
 */
@Service("powerWaveComponentsService")
public class PowerWaveComponentsServiceImpl extends BaseService implements PowerWaveComponentsService {
    @Resource
    private PowerWaveComponentsDao waveComponentsDao;

    @Override
    public void update(PowerWaveComponents waveComponents) {
        waveComponentsDao.insert(waveComponents);
    }

    @Override
    public void insert(PowerWaveComponents waveComponents) {
        waveComponentsDao.insert(waveComponents);
    }

    @Override
    public void delete(Long id) {
        waveComponentsDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<PowerWaveComponents> getComponentsList(Long waveId) {
        List<PowerWaveComponents> componentsList=  waveComponentsDao.selectComponentsByWaveId(waveId);
        return componentsList;
    }

    @Override
    public PowerWaveComponents selectById(Long id) {
        return waveComponentsDao.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateInfo(Map<String, Object> paramMap) {
        PowerWaveComponents powerWaveComponents=new PowerWaveComponents();
        Object id = paramMap.get("id");
        powerWaveComponents.setId(Long.parseLong(id.toString()));
        Boolean flag=false;
        Object componentsName = paramMap.get("name");
        Object componentsBrand = paramMap.get("brand");
        Object effectiveDate = paramMap.get("effectiveDate");
        if(componentsName!=null){
            powerWaveComponents.setName(componentsName.toString());
            flag=true;
        }
        if(componentsBrand!=null){
            powerWaveComponents.setBrand(componentsBrand.toString());
            flag=true;
        }
        if(effectiveDate!=null){
            powerWaveComponents.setEffectiveDate(effectiveDate.toString());
            flag=true;
        }
        if(flag){
            waveComponentsDao.updateByPrimaryKeySelective(powerWaveComponents);
        }
        return true;
    }
}
