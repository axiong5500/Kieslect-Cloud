package com.kieslect.user.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.vo.SaveUserInfoVO;
import com.kieslect.user.enums.KAppNotificationTypeEnum;
import com.kieslect.user.service.IUserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public R<Object> registerUserInfo(@RequestBody RegisterInfo registerInfo){
        return R.ok(userInfoService.register(registerInfo));
    }

    @PostMapping("/login")
    public R<UserInfoVO> login(@RequestBody LoginInfo loginInfo){
        return R.ok(userInfoService.login(loginInfo));
    }


    @PostMapping("/saveUserInfo")
    public R<Object> saveUserInfo(HttpServletRequest request, @RequestBody SaveUserInfoVO saveUserInfoVO){
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        saveUserInfoVO.setId(loginUser.getId());
        boolean result = userInfoService.saveUserInfo(saveUserInfoVO);
        if(!result){
            return R.fail("保存失败");
        }
        // 如果userInfoVO里的password不为空，则说明更改了密码，需要重新登录
        if(saveUserInfoVO.getPassword() != null && !saveUserInfoVO.getPassword().equals(loginUser.getPassword())){
            tokenService.delLoginUserByUserkey(loginUser.getUserKey());
            UserInfoVO entity = userInfoService.getUserInfo(loginUser.getId());
            LoginUserInfo refreshLoginUser = new LoginUserInfo();
            BeanUtils.copyProperties(entity,refreshLoginUser);
            tokenService.refreshToken(refreshLoginUser);
        }
        return R.ok();
    }

    @GetMapping("/isEmailExists/{toEmail}/{appName}")
    public R<Boolean> isEmailExists(@PathVariable("toEmail") String toEmail,@PathVariable("appName") String appName){
        return R.ok(userInfoService.isEmailExists(toEmail,appName));
    }

    @GetMapping("/notify/getAppList")
    public R<Object> getAppList() {
        Map<String, Object> map = new HashMap<>();
        map.put("appList",enumToJsonArray(KAppNotificationTypeEnum.class));
        return R.ok(map);
    }

    public static <T extends Enum<T>> JSONArray enumToJsonArray(Class<T> enumClass) {
        JSONArray jsonArray = new JSONArray();
        for (T enumValue : enumClass.getEnumConstants()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", ((KAppNotificationTypeEnum) enumValue).getCode());
            jsonObject.put("savePackName", ((KAppNotificationTypeEnum) enumValue).getSavePackName());

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }






}
