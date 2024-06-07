package com.kieslect.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.api.domain.ForgetPasswordBody;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.LogoutBody;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.user.domain.UserInfo;
import com.kieslect.user.domain.vo.SaveUserInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-03-19
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 注册
     * @param registerInfo
     * @return
     */
    Boolean register(RegisterInfo registerInfo);

    UserInfoVO login(LoginInfo loginInfo);

    boolean saveUserInfo(SaveUserInfoVO userInfoVO);

    UserInfoVO getUserInfo(long id);

    boolean isEmailExists(String email,Byte appName);

    UserInfo forgetPassword(ForgetPasswordBody userInfoVO);

    void logout(Long userId);

    UserInfoVO logoutByAccountAndPassword(LogoutBody logoutBody);

    int updateAccountStatusExpire();
}
