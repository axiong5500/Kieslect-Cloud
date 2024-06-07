package com.kieslect.user.task;

import com.kieslect.user.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-03-19
 */
@RestController
@RequestMapping("/user/task")
public class UserInfoTaskController {

    @Autowired
    IUserInfoService userInfoService;

    @GetMapping("/updateAccountStatusExpire")
    public int  updateAccountStatusExpire(){
        return userInfoService.updateAccountStatusExpire();
    }
}
