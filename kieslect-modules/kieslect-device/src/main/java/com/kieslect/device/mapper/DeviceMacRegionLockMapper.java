package com.kieslect.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kieslect.device.domain.DeviceMacRegionLock;
import com.kieslect.device.domain.vo.DeviceMacRegionLockVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-09-06
 */
public interface DeviceMacRegionLockMapper extends BaseMapper<DeviceMacRegionLock> {

    Page<DeviceMacRegionLockVO> getDeviceMacRegionLockWithPolicy(Page<?> page,@Param("macList") List<String> macList);
}
