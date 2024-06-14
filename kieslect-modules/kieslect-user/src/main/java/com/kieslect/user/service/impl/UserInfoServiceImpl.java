package com.kieslect.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.api.domain.*;
import com.kieslect.api.enums.RegisterTypeEnum;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.domain.LoginUserInfo;
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

    @Override
    public Boolean register(RegisterInfo registerInfo){
        UserInfo user = new UserInfo();
        RegisterUserInfoDTO userInfoDTO = new RegisterUserInfoDTO();
        // 根据注册方式生成账号,并判断该账号是否在数据库中，如果存在则不需要注册
        if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.EMAIL)) {
            if (isEmailExists(registerInfo.getAccount(),registerInfo.getAppName())) {
                throw new CustomException(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
            }
            userInfoDTO.setEmail(registerInfo.getAccount());
        } else if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.ACCOUNT)) {
            if (isAccountExists(registerInfo.getAccount(),registerInfo.getAppName())) {
                throw new CustomException(ResponseCodeEnum.ACCOUNT_ALREADY_EXIST);
            }
            userInfoDTO.setAccount(registerInfo.getAccount());
        } else if (RegisterTypeEnum.getByCode(registerInfo.getRegisterType()).equals(RegisterTypeEnum.THIRD_PARTY_AUTH)) {
            userInfoDTO.setThirdToken(registerInfo.getThirdPartyToken());
            userInfoDTO.setThirdTokenType(Byte.valueOf((byte) registerInfo.getThirdTokenType()));
        }
        userInfoDTO.setPassword(registerInfo.getPassword());
        userInfoDTO.setAppName(registerInfo.getAppName());

        BeanUtils.copyProperties(userInfoDTO, user);
        return this.saveOrUpdate(user);
    }

    @Override
    public UserInfoVO login(LoginInfo loginInfo) {
        String account = loginInfo.getAccount();
        Byte appName = loginInfo.getAppName();
        UserInfo userInfo;
        if (ValidationUtils.isValidEmail(account)) {
            // 邮箱登录
            userInfo = getUserInfo("email", account,appName);
        } else {
            // 账号登录
            userInfo = getUserInfo("account", account,appName);
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
        BeanUtil.copyProperties(userInfo, userInfoVO);
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
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }


    /**
     * 判断邮箱是否存在
     *
     * @param email
     * @return true:存在,false:不存在
     */
    public boolean isEmailExists(String email,Byte appName) {
        UserInfo userInfo = getUserInfo("email", email,appName);
        // 判断查询结果
        return userInfo != null;
    }

    @Override
    public UserInfo forgetPassword(ForgetPasswordBody forgetPasswordBody) {
        UserInfo userInfo = getUserInfo("email", forgetPasswordBody.getAccount(),forgetPasswordBody.getAppName());
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
            userInfo = getUserInfo("email", logoutBody.getAccount(),logoutBody.getAppName());
        } else {
            userInfo = getUserInfo("account", logoutBody.getAccount(),logoutBody.getAppName());
        }
        if (userInfo == null) {
            throw new CustomException(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
        }
        if (!userInfo.getPassword().equals(logoutBody.getPassword())) {
            throw new CustomException(ResponseCodeEnum.INCORRECT_PASSWORD);
        }
        // 删除该账号
        logout(userInfo.getId());
        BeanUtil.copyProperties(userInfo, userInfoVO,false);
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
        // 保存第三方用户信息
        ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
        BeanUtil.copyProperties(thirdLoginInfo, thirdUserInfo);
        thirdUserInfo.setThirdTokenStatus((byte) 0);
        thirdUserInfo.setCreateTime(Instant.now().getEpochSecond());
        thirdUserInfo.setUpdateTime(Instant.now().getEpochSecond());
        thirdUserInfoService.save(thirdUserInfo);

        // 注册新账号
        UserInfo user = new UserInfo();
        RegisterUserInfoDTO userInfoDTO = new RegisterUserInfoDTO();
        BeanUtil.copyProperties(userInfoDTO, user);
        this.save(user);

        //反写thirdUserInfo的userid
        thirdUserInfo.setUserId(user.getId());
        thirdUserInfoService.updateById(thirdUserInfo);

        // 用户登录
        String userKey = IdUtil.fastUUID();
        //删除旧的缓存key，实现单端登录
        tokenService.delLoginUserByUserkey(userKey);
        // 当前时间戳 单位秒
        user.setUpdateTime(Instant.now().getEpochSecond());
        user.setDelStatus((byte) 0);
        user.setUserKey(userKey);
        userInfoMapper.updateById(user);

        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtil.copyProperties(user, loginUserInfo);

        return loginUserInfo;
    }

    private UserInfo getUserInfo(String column, String value,Byte appName) {
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
    public boolean isAccountExists(String account,Byte appName) {
        // 创建查询条件
        UserInfo userInfo = getUserInfo("account", account,appName);
        // 判断查询结果
        return userInfo != null;
    }


}
