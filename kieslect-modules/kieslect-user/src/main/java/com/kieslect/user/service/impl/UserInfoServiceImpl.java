package com.kieslect.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.api.RemoteFileService;
import com.kieslect.api.domain.*;
import com.kieslect.api.enums.RegisterTypeEnum;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.core.utils.EmailUtils;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.ThirdUserInfo;
import com.kieslect.user.domain.UserInfo;
import com.kieslect.user.domain.dto.RegisterUserInfoDTO;
import com.kieslect.user.domain.vo.SaveUserInfoVO;
import com.kieslect.user.exception.CustomException;
import com.kieslect.user.mapper.UserInfoMapper;
import com.kieslect.user.service.IThirdUserInfoService;
import com.kieslect.user.service.IUserInfoService;
import com.kieslect.user.utils.ValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-03-19
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IThirdUserInfoService thirdUserInfoService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Override
    public Boolean register(RegisterInfo registerInfo) {
        UserInfo user = new UserInfo();
        RegisterUserInfoDTO registerUserInfoDTO = new RegisterUserInfoDTO();
        // 根据注册方式生成账号,并判断该账号是否在数据库中，如果存在则不需要注册
        if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.EMAIL)) {
            if (isEmailExists(registerInfo.getAccount(), registerInfo.getAppName())) {
                throw new CustomException(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
            }
            registerUserInfoDTO.setEmail(registerInfo.getAccount());
        } else if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.ACCOUNT)) {
            if (isAccountExists(registerInfo.getAccount(), registerInfo.getAppName())) {
                throw new CustomException(ResponseCodeEnum.ACCOUNT_ALREADY_EXIST);
            }
            registerUserInfoDTO.setAccount(registerInfo.getAccount());
        } else if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.THIRD_PARTY_AUTH)) {
            registerUserInfoDTO.setThirdToken(registerInfo.getThirdPartyToken());
            registerUserInfoDTO.setThirdTokenType(Byte.valueOf((byte) registerInfo.getThirdTokenType()));
        }
        registerUserInfoDTO.setIpAddress(registerInfo.getIpAddress());
        registerUserInfoDTO.setPassword(registerInfo.getPassword());
        registerUserInfoDTO.setAppName(registerInfo.getAppName());

        BeanUtils.copyProperties(registerUserInfoDTO, user);
        return this.saveOrUpdate(user);
    }

    @Override
    public UserInfoVO login(LoginInfo loginInfo) {
        String account = loginInfo.getAccount();
        Byte appName = loginInfo.getAppName();
        UserInfo userInfo;
        if (ValidationUtils.isValidEmail(account)) {
            // 邮箱登录
            userInfo = getUserInfo("email", account, appName);
        } else {
            // 账号登录
            userInfo = getUserInfo("account", account, appName);
        }
        // 账号不存在
        if (userInfo == null) {
            throw new CustomException(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
        }
        // 密码校验
        if (!userInfo.getPassword().equals(loginInfo.getPassword())) {
            throw new CustomException(ResponseCodeEnum.INCORRECT_PASSWORD);
        }
        // 密码正确 ,删除旧的缓存key，实现单端登录
        tokenService.delLoginUserByUserkey(userInfo.getUserKey());

        // 当前时间戳 单位秒
        userInfo.setUpdateTime(Instant.now().getEpochSecond());
        userInfo.setDelStatus((byte) 0);
        userInfo.setUserKey(loginInfo.getUserKey());
        userInfoMapper.updateById(userInfo);

        // 封装返回值
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO, CopyOptions.create().setFieldMapping(
                Map.of("id", "kid")
        ));
        return userInfoVO;
    }

    @Override
    public boolean saveUserInfo(SaveUserInfoVO userInfoVO) {
        // firstLogin 修改为1
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVO, userInfo);
        userInfo.setFirstLogin((byte) 0).setUpdateTime(Instant.now().getEpochSecond());
        return this.updateById(userInfo);
    }

    @Override
    public UserInfoVO getUserInfo(long id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo, userInfoVO, CopyOptions.create().setFieldMapping(
                Map.of("id", "kid")
        ));
        return userInfoVO;
    }


    /**
     * 判断邮箱是否存在
     *
     * @param email
     * @return true:存在,false:不存在
     */
    public boolean isEmailExists(String email, Byte appName) {
        UserInfo userInfo = getUserInfo("email", email, appName);
        // 判断查询结果
        return userInfo != null;
    }

    @Override
    public UserInfo forgetPassword(ForgetPasswordBody forgetPasswordBody) {
        UserInfo userInfo = getUserInfo("email", forgetPasswordBody.getAccount(), forgetPasswordBody.getAppName());
        if (userInfo != null) {
            userInfo.setPassword(forgetPasswordBody.getPassword());
            userInfoMapper.updateById(userInfo);
        }
        return userInfo;
    }

    @Override
    public void logout(Long userId) {
        this.updateById(new UserInfo().setId(userId).setDelStatus((byte) 1).setUpdateTime(Instant.now().getEpochSecond()));
    }

    @Override
    public UserInfoVO logoutByAccountAndPassword(LogoutBody logoutBody) {
        if (logoutBody == null) {
            throw new CustomException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (logoutBody.getAccount() == null || logoutBody.getPassword() == null) {
            throw new CustomException(ResponseCodeEnum.PARAM_ERROR);
        }
        // 通过logoutBody.getAccount() 和 logoutBody.getAppName() 查询数据库里的账号或者邮箱是否存在该账号
        UserInfoVO userInfoVO = new UserInfoVO();
        UserInfo userInfo;
        if (EmailUtils.isEmail(logoutBody.getAccount())) {
            userInfo = getUserInfo("email", logoutBody.getAccount(), logoutBody.getAppName());
        } else {
            userInfo = getUserInfo("account", logoutBody.getAccount(), logoutBody.getAppName());
        }
        if (userInfo == null) {
            throw new CustomException(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
        }
        if (!userInfo.getPassword().equals(logoutBody.getPassword())) {
            throw new CustomException(ResponseCodeEnum.INCORRECT_PASSWORD);
        }
        // 删除该账号
        logout(userInfo.getId());
        BeanUtil.copyProperties(userInfo, userInfoVO, false);
        return userInfoVO;
    }

    @Override
    @Transactional
    public int updateAccountStatusExpire() {
        long sevenDaysAgoTimestamp = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);

        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("del_status", 2)
                .eq("del_status", 1)
                .lt("update_time", sevenDaysAgoTimestamp);

        return userInfoMapper.update(null, updateWrapper);
    }

    @Override
    public LoginUserInfo thirdLogin(ThirdLoginInfo thirdLoginInfo) {
        if (thirdLoginInfo == null) {
            throw new IllegalArgumentException("ThirdLoginInfo cannot be null");
        }

        LoginUserInfo loginUserInfo = new LoginUserInfo();
        try {
            // 统一获取当前时间戳
            long currentTime = Instant.now().getEpochSecond();

            // 检查是否存在已绑定的第三方用户信息
            ThirdUserInfo existingBinding = getExistingBinding(thirdLoginInfo);
            if (existingBinding != null) {
                /* 更新已存在的绑定信息，以反映最新的登录细节 */
                updateExistingBinding(thirdLoginInfo, existingBinding, currentTime);

                /* 根据现有的绑定信息获取用户详情 */
                UserInfo userInfo = getUserInfo(existingBinding.getUserId());

                /* 登录后处理与绑定相关的逻辑，例如更新登录状态或时间 */
                loginAfterBinding(userInfo, currentTime);

                /* 将登录后的用户信息更新到登录用户信息对象中 */
                BeanUtil.copyProperties(userInfo, loginUserInfo,CopyOptions.create().setFieldMapping(
                        Map.of("id", "kid")
                ));

            } else {
                // 保存第三方用户信息并注册新账号
                ThirdUserInfo thirdUserInfo = saveNewThirdUserInfo(thirdLoginInfo, currentTime);
                UserInfo user = registerNewUser(thirdUserInfo);
                loginAfterRegistration(user, currentTime);
                BeanUtil.copyProperties(user, loginUserInfo,CopyOptions.create().setFieldMapping(
                        Map.of("id", "kid")
                ));
            }
        } catch (Exception e) {
            // 异常处理逻辑，如记录日志、抛出自定义异常等
            throw new RuntimeException("Error processing third party login", e);
        }

        return loginUserInfo;
    }

    private ThirdUserInfo getExistingBinding(ThirdLoginInfo thirdLoginInfo) {
        LambdaQueryWrapper<ThirdUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdUserInfo::getThirdId, thirdLoginInfo.getThirdId())
                .eq(ThirdUserInfo::getAppName, thirdLoginInfo.getAppName())
                .eq(ThirdUserInfo::getThirdTokenType, thirdLoginInfo.getThirdTokenType());
        return thirdUserInfoService.getOneOpt(queryWrapper).orElse(null);
    }

    private void updateExistingBinding(ThirdLoginInfo thirdLoginInfo, ThirdUserInfo existingBinding, long currentTime) {
        BeanUtil.copyProperties(thirdLoginInfo, existingBinding, "thirdId", "localPictureUrl");
        existingBinding.setUpdateTime(currentTime);
        thirdUserInfoService.updateById(existingBinding);
    }

    private UserInfo getUserInfo(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    private void loginAfterBinding(UserInfo userInfo, long currentTime) {
        // 删除旧的userKey缓存，实现单端登录
        tokenService.delLoginUserByUserkey(userInfo.getUserKey());
        userInfo.setUpdateTime(currentTime);
        userInfo.setDelStatus((byte) 0);
        userInfo.setUserKey(IdUtil.fastUUID());
        userInfoMapper.updateById(userInfo);
    }

    private ThirdUserInfo saveNewThirdUserInfo(ThirdLoginInfo thirdLoginInfo, long currentTime) {
        ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
        BeanUtil.copyProperties(thirdLoginInfo, thirdUserInfo);
        if (!StrUtil.isEmpty(thirdUserInfo.getPictureUrl())) {
            // hutool下载图片到本地并上传到oss获得图片地址
            R<?> result = remoteFileService.remoteUrlToOSS(thirdUserInfo.getPictureUrl(), 0);
            if (R.isSuccess(result)) {
                if (result.getData() != null) {
                    String filePath = ((HashMap<String, Object>) result.getData()).get("fileUrl").toString();
                    thirdUserInfo.setLocalPictureUrl(filePath);
                }
            }
        }
        thirdUserInfo.setThirdTokenStatus((byte) 0);
        thirdUserInfo.setCreateTime(currentTime);
        thirdUserInfo.setUpdateTime(currentTime);
        thirdUserInfoService.save(thirdUserInfo);
        return thirdUserInfo;
    }

    private UserInfo registerNewUser(ThirdUserInfo thirdUserInfo) {
        UserInfo user = new UserInfo();
        RegisterUserInfoDTO userInfoDTO = new RegisterUserInfoDTO();
        userInfoDTO.setAppName(thirdUserInfo.getAppName());
        BeanUtil.copyProperties(userInfoDTO, user);
        this.save(user);

        thirdUserInfo.setUserId(user.getId());
        thirdUserInfoService.updateById(thirdUserInfo);

        user.setUpdateTime(Instant.now().getEpochSecond());
        user.setDelStatus((byte) 0);
        user.setUserKey(IdUtil.fastUUID());
        userInfoMapper.updateById(user);

        return user;
    }

    private void loginAfterRegistration(UserInfo user, long currentTime) {
        user.setUpdateTime(currentTime);
        user.setDelStatus((byte) 0);
        user.setUserKey(IdUtil.fastUUID());
        userInfoMapper.updateById(user);
    }


    private UserInfo getUserInfo(String column, String value, Byte appName) {
        // 创建查询条件
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        // 如果需要查询特定应用下的用户信息，则添加应用名称作为查询条件
        if (appName != null) {
            queryWrapper.eq("app_name", appName);
        }
        queryWrapper.ne("del_status", 2);
        // 执行查询
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        return userInfo;
    }

    /**
     * 判断账号是否存在
     *
     * @param account
     * @return true:存在,false:不存在
     */
    public boolean isAccountExists(String account, Byte appName) {
        // 创建查询条件
        UserInfo userInfo = getUserInfo("account", account, appName);
        // 判断查询结果
        return userInfo != null;
    }


}
