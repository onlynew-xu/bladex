package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerBoxLoop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerBoxLoopDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerBoxLoop record);

    int insertSelective(PowerBoxLoop record);

    PowerBoxLoop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerBoxLoop record);

    int updateByPrimaryKey(PowerBoxLoop record);

    List<PowerBoxLoop> selectBoxLoop(Long boxId);

    int deleteByBoxId(@Param("boxId") Long boxId);

    PowerBoxLoop getBoxLoopInfoByBoxAndName(@Param("boxId") Long boxId,@Param("loopName") String loopName);

    List<PowerBoxLoop> getBoxLoopList(@Param("boxId") Long boxId);
}