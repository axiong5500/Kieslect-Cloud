package com.kieslect.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.kieslect.api.domain.*;
import com.kieslect.api.model.ThirdUserInfoVO;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.EmailTypeEnum;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.mail.service.MailService;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.ThirdUserInfo;
import com.kieslect.user.domain.UserInfo;
import com.kieslect.user.domain.vo.BindEmailVO;
import com.kieslect.user.domain.vo.BindThirdInfoVO;
import com.kieslect.user.domain.vo.ChangeEmailVO;
import com.kieslect.user.domain.vo.SaveUserInfoVO;
import com.kieslect.user.enums.KAppNotificationTypeEnum;
import com.kieslect.user.service.IThirdUserInfoService;
import com.kieslect.user.service.IUserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-03-19
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IThirdUserInfoService thirdUserInfoService;

    @PostMapping("/register")
    public R<Object> registerUserInfo(@RequestBody RegisterInfo registerInfo) {
        return R.ok(userInfoService.register(registerInfo));
    }

    @PostMapping("/login")
    public R<UserInfoVO> login(@RequestBody LoginInfo loginInfo) {
        return R.ok(userInfoService.login(loginInfo));
    }

    @PostMapping("/third/login")
    public R<?> thirdLogin(@RequestBody ThirdLoginInfo thirdLoginInfo) {
        LoginUserInfo userInfo = userInfoService.thirdLogin(thirdLoginInfo);
        return R.ok(tokenService.createToken(userInfo));
    }

    @PostMapping("/third/binding")
    public R<?> thirdBinding(HttpServletRequest request,@RequestBody BindThirdInfoVO bindThirdInfoVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getId();

        // 检查是否存在已绑定的第三方用户信息
        Optional<ThirdUserInfo> existingBinding = thirdUserInfoService.findByUserIdAndThirdId(userId, bindThirdInfoVO.getThirdId());

        if (existingBinding.isPresent()) {
            // 如果存在，更新现有记录
            ThirdUserInfo thirdUserInfo = existingBinding.get();
            BeanUtil.copyProperties(bindThirdInfoVO, thirdUserInfo, "thirdId"); // 不覆盖第三方ID
            thirdUserInfo.setUpdateTime(Instant.now().getEpochSecond());
            boolean result = thirdUserInfoService.updateById(thirdUserInfo);

            return R.ok(result);
        } else {
            // 如果不存在，创建新记录
            ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
            BeanUtil.copyProperties(bindThirdInfoVO, thirdUserInfo);
            thirdUserInfo.setUserId(userId);
            thirdUserInfo.setThirdTokenStatus((byte) 0);
            thirdUserInfo.setCreateTime(Instant.now().getEpochSecond());
            thirdUserInfo.setUpdateTime(Instant.now().getEpochSecond());

            boolean result = thirdUserInfoService.save(thirdUserInfo);
            return R.ok(result);
        }
    }

    @PostMapping("/third/unBinding/{id}")
    public R<?> thirdUnBinding(@PathVariable Long id) {
        return R.ok(thirdUserInfoService.removeById(id));
    }

    @PostMapping("/third/getThirdUserInfo/{userId}")
    public R<List<ThirdUserInfoVO>> getThirdUserInfo(@PathVariable Long userId) {
        List<ThirdUserInfo> thirdUserInfoList = thirdUserInfoService.getThirdUserInfo(userId);
        if (thirdUserInfoList != null) {
            List<ThirdUserInfoVO> thirdUserInfoVOList = thirdUserInfoList.stream().map(thirdUserInfo -> {
                ThirdUserInfoVO thirdUserInfoVO = new ThirdUserInfoVO();
                BeanUtils.copyProperties(thirdUserInfo, thirdUserInfoVO);
                return thirdUserInfoVO;
            }).toList();
            return R.ok(thirdUserInfoVOList);
        }
        return R.ok();
    }



    @PostMapping("/saveUserInfo")
    public R<Object> saveUserInfo(HttpServletRequest request, @RequestBody SaveUserInfoVO saveUserInfoVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        saveUserInfoVO.setId(loginUser.getId());
        boolean result = userInfoService.saveUserInfo(saveUserInfoVO);
        if (!result) {
            return R.fail("保存失败");
        }
        // 如果userInfoVO里的password不为空，则说明更改了密码，需要重新登录
        if (saveUserInfoVO.getPassword() != null && !saveUserInfoVO.getPassword().equals(loginUser.getPassword())) {
            tokenService.delLoginUserByUserkey(loginUser.getUserKey());
        }else{
            UserInfoVO entity = userInfoService.getUserInfo(loginUser.getId());
            LoginUserInfo refreshLoginUser = new LoginUserInfo();
            BeanUtils.copyProperties(entity, refreshLoginUser);
            tokenService.refreshToken(refreshLoginUser);
        }

        return R.ok();
    }

    /**
     * 绑定邮箱
     * 校验邮箱是否已经存在数据库的校验步骤已经在发送验证码环节处理，所以不需要二次校验
     *
     * @param request
     * @param bindEmailVO
     * @return
     */
    @PostMapping("/bindEmail")
    public R<Object> bindEmail(HttpServletRequest request, @RequestBody BindEmailVO bindEmailVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        String email = bindEmailVO.getEmail();
        String code = bindEmailVO.getCode();
        // 校验验证码是否正确
        String emailRediskey = EmailTypeEnum.BIND_EMAIL.getRedisKey() + email;
        boolean validCode = mailService.isCaptchaValid(emailRediskey, code);

        if (!validCode) {
            return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
        }


        SaveUserInfoVO saveUserInfoVO = new SaveUserInfoVO().setId(loginUser.getId()).setEmail(email);
        // 保存用户信息
        userInfoService.saveUserInfo(saveUserInfoVO);
        // 刷新缓存
        loginUser.setEmail(email);
        tokenService.refreshToken(loginUser);
        return R.ok();
    }

    // 旧邮箱换新邮箱
    @PostMapping("/changeEmail")
    public R<Object> changeEmail(HttpServletRequest request, @RequestBody @Valid ChangeEmailVO changeEmailVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        String oldEmail = loginUser.getEmail();
        String newEmail = changeEmailVO.getNewEmail();
        String oldCode = changeEmailVO.getOldCode();
        String newCode = changeEmailVO.getNewCode();

        boolean isBindNewEmail = StrUtil.isEmpty(oldEmail);
        String oldEmailRediskey = null;

        // 如果oldEmail为空，则说明是绑定新邮箱
        if (!isBindNewEmail) {
            // 校验旧验证码是否正确
            oldEmailRediskey = EmailTypeEnum.CHANGE_EMAIL_OLD.getRedisKey() + oldEmail;
            boolean validCode = mailService.isCaptchaValid(oldEmailRediskey, oldCode);
            if (!validCode) {
                return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
            }
        }

        // 校验新验证码是否正确
        String newEmailRediskey = EmailTypeEnum.BIND_EMAIL.getRedisKey() + newEmail;
        boolean validCode = mailService.isCaptchaValid(newEmailRediskey, newCode);
        if (!validCode) {
            return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
        }

        if (!isBindNewEmail) {
            redisService.deleteObject(oldEmailRediskey);
        }

        redisService.deleteObject(newEmailRediskey);

        SaveUserInfoVO saveUserInfoVO = new SaveUserInfoVO().setId(loginUser.getId()).setEmail(newEmail);
        userInfoService.saveUserInfo(saveUserInfoVO);

        // 刷新缓存
        loginUser.setEmail(newEmail);
        tokenService.refreshToken(loginUser);
        return R.ok();
    }


    @PostMapping("/forgetPassword")
    public R<Object> forgetPassword(@RequestBody ForgetPasswordBody forgetPasswordBody) {

        UserInfo userInfo = userInfoService.forgetPassword(forgetPasswordBody);

        // 如果userInfoVO里的password不为空，则说明更改了密码，需要重新登录
        if (forgetPasswordBody.getPassword() != null) {
            tokenService.delLoginUserByUserkey(userInfo.getUserKey());
        }
        return R.ok();
    }

    @GetMapping("/isEmailExists/{toEmail}/{appName}")
    public R<Boolean> isEmailExists(@PathVariable("toEmail") String toEmail, @PathVariable("appName") Byte appName) {
        return R.ok(userInfoService.isEmailExists(toEmail, appName));
    }

    @PostMapping("/logout/{userId}")
    public void logout(@PathVariable("userId") Long userId) {
        userInfoService.logout(userId);
    }


    @PostMapping("/logoutByAccountAndPassword")
    public R<Object> logoutByAccountAndPassword(@RequestBody LogoutBody logoutBody) {
        return R.ok(userInfoService.logoutByAccountAndPassword(logoutBody));
    }

    @GetMapping("/notify/getAppList")
    public R<Object> getAppList() {
        Map<String, Object> map = new HashMap<>();
        map.put("appList", enumToJsonArray(KAppNotificationTypeEnum.class));
        return R.ok(map);
    }

    public static <T extends Enum<T>> JSONArray enumToJsonArray(Class<T> enumClass) {
        JSONArray jsonArray = new JSONArray();
        for (T enumValue : enumClass.getEnumConstants()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", ((KAppNotificationTypeEnum) enumValue).getCode());
            jsonObject.put("savePackName", ((KAppNotificationTypeEnum) enumValue).getSavePackName());
            jsonObject.put("uteValue", ((KAppNotificationTypeEnum) enumValue).getValue());
            jsonObject.put("desc", ((KAppNotificationTypeEnum) enumValue).getDesc());
            jsonObject.put("icon", ((KAppNotificationTypeEnum) enumValue).getIcon());

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


}
