package com.kieslect.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.device.domain.DeviceActiveInfo;
import com.kieslect.device.domain.ThirdUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备激活数据表 Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-08-21
 */
public interface DeviceActiveInfoMapper extends BaseMapper<DeviceActiveInfo> {

    void insertIgnoreBatch(@Param("deviceActiveInfoList")List<DeviceActiveInfo> deviceActiveInfoList);

    List<ThirdUserInfo> searchThirdUserInfoByUserId(Long kid);
}
