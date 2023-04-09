package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerComponentsDao;
import com.steelman.iot.platform.entity.Company;
import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.PowerComponents;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.service.CompanyService;
import com.steelman.iot.platform.service.PowerComponentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/11 0011 17:35
 * @Description:
 */
@Service("powerComponentsService")
public class PowerComponentsServiceImpl extends BaseService implements PowerComponentsService {

    @Resource
    private PowerComponentsDao powerComponentsDao;
    @Resource
    private CompanyService companyService;

    @Override
    public void insert(PowerComponents components) {
        powerComponentsDao.insert(components);
    }

    @Override
    public void update(PowerComponents components) {
        powerComponentsDao.updateByPrimaryKeySelective(components);
    }

    @Override
    public void delete(Long id) {
        powerComponentsDao.deleteByPrimaryKey(id);
    }


    @Override
    public List<PowerComponents> getComponentsList(Long id, Integer type, Integer powerDeviceType) {
        return  powerComponentsDao.selectDeviceComponents(id,type,powerDeviceType);
    }

    @Override
    public List<PowerComponentsDto> selectComponentsByPowerType(Integer powerType, Long powerDeviceId, Integer componentType,Long projectId) {
        List<PowerComponents> powerComponentsList = powerComponentsDao.selectComponentsByPowerType(powerType, powerDeviceId, componentType);
        if(!CollectionUtils.isEmpty(powerComponentsList)){
            List<Company> companyList = companyService.findByProjectId(projectId, 0);
            Map<Long,String>  companyMap=new HashMap<>();
            if(!CollectionUtils.isEmpty(companyList)){
                for (Company company : companyList) {
                    companyMap.put(company.getId(),company.getName());
                }
            }
            List<PowerComponentsDto>  powerComponentsDtoList=new ArrayList<>();
            for (PowerComponents powerComponents : powerComponentsList) {
                PowerComponentsDto powerComponentsDto=new PowerComponentsDto(powerComponents);
                Long installationCompanyId = powerComponentsDto.getInstallationCompanyId();
                if(installationCompanyId!=null && !installationCompanyId.equals(0l)){
                    powerComponentsDto.setInstallationCompanyName(companyMap.get(installationCompanyId));
                }
                Long maintenanceCompanyId = powerComponentsDto.getMaintenanceCompanyId();
                if(maintenanceCompanyId!=null && !maintenanceCompanyId.equals(0l)){
                    powerComponentsDto.setMaintenanceCompanyName(companyMap.get(maintenanceCompanyId));
                }
                Long useCompanyId = powerComponentsDto.getUseCompanyId();
                if(useCompanyId!=null && !useCompanyId.equals(0l)){
                    powerComponentsDto.setUserCompanyName(companyMap.get(useCompanyId));
                }
                powerComponentsDtoList.add(powerComponentsDto);
            }
            return  powerComponentsDtoList;
        }
        return null;
    }

    @Override
    public PowerComponentsDto getComponentsInfo(Long projectId, Long componentsId) {
        PowerComponents powerComponents = powerComponentsDao.selectByPrimaryKey(componentsId);
        if(powerComponents!=null){
            PowerComponentsDto powerComponentsDto=new PowerComponentsDto(powerComponents);
            List<Company> companyList = companyService.findByProjectId(projectId, 0);
            Map<Long,String>  companyMap=new HashMap<>();
            if(!CollectionUtils.isEmpty(companyList)){
                for (Company company : companyList) {
                    companyMap.put(company.getId(),company.getName());
                }
            }
            Integer type = powerComponents.getType();
            if(type.equals(1)){
                powerComponentsDto.setTypeName("开关类型");
            }else if(type.equals(2)){
                powerComponentsDto.setTypeName("避雷类型");
            }
            Long installationCompanyId = powerComponents.getInstallationCompanyId();
            if(installationCompanyId!=null && !installationCompanyId.equals(0l)){
                powerComponentsDto.setInstallationCompanyName(companyMap.get(installationCompanyId));
            }
            Long maintenanceCompanyId = powerComponents.getMaintenanceCompanyId();
            if(maintenanceCompanyId!=null && !maintenanceCompanyId.equals(0l)){
                powerComponentsDto.setMaintenanceCompanyName(companyMap.get(maintenanceCompanyId));
            }
            Long useCompanyId = powerComponents.getUseCompanyId();
            if(useCompanyId!=null && !useCompanyId.equals(0l)){
                powerComponentsDto.setUserCompanyName(companyMap.get(useCompanyId));
            }
            return powerComponentsDto;
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateComponents(Map<String, Object> paramMap) {
        PowerComponents powerComponents=new PowerComponents();
        Boolean flag=false;
        Boolean installationCompanyIdFlag=false;
        Boolean useCompanyIdFlag=false;
        Boolean maintenanceCompanyIdFlag=false;
        Object componentsId = paramMap.get("componentsId");
        Object name = paramMap.get("name");
        Object brand = paramMap.get("brand");
        Object manufacturer = paramMap.get("manufacturer");
        Object productDate = paramMap.get("productDate");
        Object usefulLife = paramMap.get("usefulLife");
        Object effectiveDate = paramMap.get("effectiveDate");
        Object installationCompanyId = paramMap.get("installationCompanyId");
        Object useCompanyId = paramMap.get("useCompanyId");
        Object maintenanceCompanyId = paramMap.get("maintenanceCompanyId");
        powerComponents.setId(Long.parseLong(componentsId.toString()));
        if(name!=null){
            flag=true;
            powerComponents.setName(name.toString());
        }
        if(brand!=null){
            flag=true;
            powerComponents.setBrand(brand.toString());
        }
        if(manufacturer!=null){
            flag=true;
            powerComponents.setManufacturer(manufacturer.toString());
        }
        if(productDate!=null){
            flag=true;
            powerComponents.setProductDate(productDate.toString());
        }
        if(usefulLife!=null){
            flag=true;
            powerComponents.setUsefulLife(usefulLife.toString());
        }
        if(effectiveDate!=null){
            flag=true;
            powerComponents.setEffectiveDate(effectiveDate.toString());
        }
        if(installationCompanyId!=null){
            if(installationCompanyId.toString().equals("0")){
                installationCompanyIdFlag=true;
            }else{
                flag=true;
                powerComponents.setInstallationCompanyId(Long.parseLong(installationCompanyId.toString()));
            }
        }
        if(useCompanyId!=null){
            if(useCompanyId.toString().equals("0")){
                useCompanyIdFlag=true;
            }else{
                flag=true;
                powerComponents.setUseCompanyId(Long.parseLong(useCompanyId.toString()));
            }
        }
        if(maintenanceCompanyId!=null){
            if(maintenanceCompanyId.toString().equals("0")){
                maintenanceCompanyIdFlag=true;
            }else{
                flag=true;
                powerComponents.setMaintenanceCompanyId(Long.parseLong(maintenanceCompanyId.toString()));
            }
        }
        if(flag){
            powerComponentsDao.updateByPrimaryKeySelective(powerComponents);
        }
        if(installationCompanyIdFlag){
            powerComponentsDao.updateInstallationNull(Long.parseLong(componentsId.toString()));
        }
        if(useCompanyIdFlag){
            powerComponentsDao.updateUseCompanyNull(Long.parseLong(componentsId.toString()));
        }
        if(maintenanceCompanyIdFlag){
            powerComponentsDao.updateMaintenanceNull(Long.parseLong(componentsId.toString()));
        }
        return true;
    }

    @Override
    public List<PowerCompensate> findByPowerId(Long powerId) {
        return null;
    }
}
