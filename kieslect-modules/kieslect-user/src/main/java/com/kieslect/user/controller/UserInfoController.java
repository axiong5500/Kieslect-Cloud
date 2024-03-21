package com.kieslect.user.controller;

import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.domain.R;
import com.kieslect.user.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-03-19
 */
@RestController
@RequestMapping()
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;
    @PostMapping("/register")
    public R<Object> registerUserInfo(@RequestBody RegisterInfo registerInfo){
        return R.ok(userInfoService.register(registerInfo));
    }

    @PostMapping("/login")
    public R<UserInfoVO> login(@RequestBody LoginInfo loginInfo){
        return R.ok(userInfoService.login(loginInfo));
    }
}
