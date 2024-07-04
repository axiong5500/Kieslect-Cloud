package com.kieslect.outapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.outapi.domain.ThirdDeviceActiveInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-06-24
 */
@Mapper
public interface ThirdDeviceActiveInfoMapper extends BaseMapper<ThirdDeviceActiveInfo> {

    /**
     * 批量插入或更新
     * @param records
     */
    @Insert("<script>" +
            "INSERT INTO t_third_device_active_info (device_id, device_alias, country, province, city, " +
            "operating_system, phone_model, gender, birthdate, activation_time, activation_timezone, " +
            "mac_address, serial_number, ip_address, firmware_version, data_source, account_type, create_time, update_time) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(" +
            "#{item.deviceId}, #{item.deviceAlias}, #{item.country}, #{item.province}, #{item.city}, " +
            "#{item.operatingSystem}, #{item.phoneModel}, #{item.gender}, #{item.birthdate}, #{item.activationTime}, #{item.activationTimezone}, " +
            "#{item.macAddress}, #{item.serialNumber}, #{item.ipAddress}, #{item.firmwareVersion}, #{item.dataSource}, #{item.accountType}, " +
            "#{item.createTime}, #{item.updateTime}" +
            ")" +
            "</foreach>" +
            "ON DUPLICATE KEY UPDATE " +
            "device_alias = VALUES(device_alias), " +
            "country = VALUES(country), " +
            "province = VALUES(province), " +
            "city = VALUES(city), " +
            "operating_system = VALUES(operating_system), " +
            "phone_model = VALUES(phone_model), " +
            "gender = VALUES(gender), " +
            "birthdate = VALUES(birthdate), " +
            "activation_time = VALUES(activation_time), " +
            "activation_timezone = VALUES(activation_timezone), " +
            "serial_number = VALUES(serial_number), " +
            "ip_address = VALUES(ip_address), " +
            "firmware_version = VALUES(firmware_version), " +
            "data_source = VALUES(data_source), " +
            "account_type = VALUES(account_type), " +
            "update_time = VALUES(update_time)" +
            "</script>")
    boolean insertOrUpdateBatch(@Param("list")List<ThirdDeviceActiveInfo> records);
}
