package com.kieslect.device.service.async;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.device.domain.DeviceActiveInfo;
import com.kieslect.device.domain.DeviceBinding;
import com.kieslect.device.domain.ThirdUserInfo;
import com.kieslect.device.service.IDeviceActiveInfoService;
import com.kieslect.device.utils.GeoIPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DeviceActivationService {
    @Autowired
    private IDeviceActiveInfoService deviceActiveInfoService;
    /**
     * 异步保存设备激活信息
     *
     * @param deviceBindings 设备绑定列表
     * @param loginUser      登录用户信息
     * @param clientIp       客户端IP地址
     */
    @Async("taskExecutor")
    public void AsyncSaveDeviceActivationInfo(List<DeviceBinding> deviceBindings, LoginUserInfo loginUser, String clientIp) {
        // 组装入库的数据
        List<DeviceActiveInfo> deviceActiveInfoList = new ArrayList<>();

        String country = GeoIPUtil.getCountryCode(clientIp);
        String province = GeoIPUtil.getProvince(clientIp);
        String city = GeoIPUtil.getCity(clientIp);
        String operatingSystem = String.valueOf(loginUser.getAppSystem());
        String phoneModel = loginUser.getPhoneType();
        String gender = String.valueOf(loginUser.getSex());
        // 判断是不是android手机上传上来的性别需进行转换
        if (Objects.isNull(loginUser.getNewSex()) && loginUser.getSex() != null) {
            if (loginUser.getAppSystem() == 0){
                gender = String.valueOf(loginUser.getSex() == 0 ? (byte) 1 : (loginUser.getSex() == 1 ? (byte) 0 : (byte) 2));
            }else{
                gender = String.valueOf(loginUser.getSex());
            }
        }

        LocalDate birthdate = getBirthdate(loginUser.getBirthday());
        String activationTimezone = "UTC+8";
        String ipAddress = clientIp;
        String dataSource = "0";
        Byte appName = loginUser.getAppName();
        Byte accountType = getAccountTypeByUserId(loginUser);

        for (DeviceBinding deviceBinding : deviceBindings) {
            if (StrUtil.isBlank(deviceBinding.getDeviceId())) {
                continue;
            }
            DeviceActiveInfo deviceActiveInfo = new DeviceActiveInfo();
            deviceActiveInfo.setUserId(Math.toIntExact(loginUser.getKid()));
            deviceActiveInfo.setBindingId(deviceBinding.getId());
            deviceActiveInfo.setDeviceId(deviceBinding.getDeviceId());
            deviceActiveInfo.setDeviceAlias(deviceBinding.getDeviceName());
            deviceActiveInfo.setCountry(country);
            deviceActiveInfo.setProvince(province);
            deviceActiveInfo.setCity(city);
            deviceActiveInfo.setOperatingSystem(operatingSystem);
            deviceActiveInfo.setPhoneModel(phoneModel);
            deviceActiveInfo.setGender(gender);
            deviceActiveInfo.setBirthdate(birthdate);
            deviceActiveInfo.setActivationTime(deviceBinding.getActiveTime());
            deviceActiveInfo.setActivationTimezone(activationTimezone);
            deviceActiveInfo.setMacAddress(deviceBinding.getMac());
            deviceActiveInfo.setIpAddress(ipAddress);
            deviceActiveInfo.setFirmwareVersion(deviceBinding.getDeviceVersion());
            deviceActiveInfo.setDataSource(dataSource);
            deviceActiveInfo.setAppName(appName);
            deviceActiveInfo.setAccountType(accountType);

            deviceActiveInfoList.add(deviceActiveInfo);
        }

        deviceActiveInfoService.insertIgnoreBatch(deviceActiveInfoList);
    }

    private LocalDate getBirthdate(Long birthday) {
        if (birthday == null) {
            return null;
        } else {
            try {
                long millis = birthday * 1000;
                Date date = DateUtil.date(millis);
                return DateUtil.toLocalDateTime(date).toLocalDate();
            }catch (Exception e){
                return null;
            }
        }
    }

    private Byte getAccountTypeByUserId(LoginUserInfo loginUser) {
        Long kid = loginUser.getKid();
        String account = loginUser.getAccount();
        Long createTime = loginUser.getCreateTime();
        List<ThirdUserInfo> thirdUserInfoList = deviceActiveInfoService.searchThirdUserInfoByUserId(kid);

        if (CollUtil.isNotEmpty(thirdUserInfoList)) {
            if (createTime.compareTo(thirdUserInfoList.stream().min(Comparator.comparing(ThirdUserInfo::getCreateTime)).get().getCreateTime()) < 0) {
                return Byte.valueOf("4"); // 账号注册
            } else {
                return Byte.valueOf("5"); // 邮箱注册
            }
        } else {
            if (StrUtil.isNotBlank(account) && isRegistAccount(account)) {
                return Byte.valueOf("4"); // 账号注册
            } else {
                return Byte.valueOf("5"); // 邮箱注册
            }
        }
    }

    private boolean isRegistAccount(String account) {
        try {
            long timestamp = Long.parseLong(account);
            long currentTimeMillis = System.currentTimeMillis();
            return timestamp <= currentTimeMillis && timestamp >= 0;
        } catch (Exception e) {
            return false;
        }
    }

}
