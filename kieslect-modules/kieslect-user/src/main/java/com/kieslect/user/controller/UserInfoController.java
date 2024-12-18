package com.kieslect.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
import com.kieslect.user.domain.Icon;
import com.kieslect.user.domain.ThirdUserInfo;
import com.kieslect.user.domain.UserInfo;
import com.kieslect.user.domain.vo.*;
import com.kieslect.user.exception.CustomException;
import com.kieslect.user.service.IIconService;
import com.kieslect.user.service.IThirdUserInfoService;
import com.kieslect.user.service.IUserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(UserInfoController.class);

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

    @Autowired
    private IIconService iconService;

    @PostMapping("/register")
    public R<Object> registerUserInfo( @RequestBody RegisterInfo registerInfo, @RequestHeader("X-Client-IP") String clientIp) {
        logger.info("客户端IP：" +clientIp);
        registerInfo.setIpAddress(clientIp);
        return R.ok(userInfoService.register(registerInfo));
    }

    @PostMapping("/login")
    public R<UserInfoVO> login(@RequestBody LoginInfo loginInfo) {
        return R.ok(userInfoService.login(loginInfo));
    }

    @PostMapping("/third/login")
    public R<?> thirdLogin(@RequestBody ThirdLoginInfo thirdLoginInfo, @RequestHeader("X-Client-IP") String clientIp) {
        thirdLoginInfo.setIpAddress(clientIp);
        LoginUserInfo userInfo = userInfoService.thirdLogin(thirdLoginInfo);
        return R.ok(tokenService.createToken(userInfo));
    }

    @PostMapping("/third/binding")
    public R<?> thirdBinding(HttpServletRequest request, @RequestBody BindThirdInfoVO bindThirdInfoVO) {
        Map<String, Object> map = new HashMap<>();
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();


        // 检查是否存在已绑定的第三方用户信息
        Optional<ThirdUserInfo> existingBinding = thirdUserInfoService.findByUserIdAndThirdId(bindThirdInfoVO.getThirdId(),bindThirdInfoVO.getThirdTokenType());

        if (existingBinding.isPresent()) {
            // 如果存在
            throw new CustomException(ResponseCodeEnum.ACCOUNT_ALREADY_BIND);
        } else {
            // 如果不存在，创建新记录
            ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
            BeanUtil.copyProperties(bindThirdInfoVO, thirdUserInfo);
            thirdUserInfo.setUserId(userId);
            thirdUserInfo.setThirdTokenStatus((byte) 0);
            thirdUserInfo.setCreateTime(Instant.now().getEpochSecond());
            thirdUserInfo.setUpdateTime(Instant.now().getEpochSecond());

            boolean result = thirdUserInfoService.save(thirdUserInfo);
            logger.info("保存第三方用户信息成功: {},{}", result, thirdUserInfo);
        }
        List<ThirdUserInfoVO> thirdUserInfoList = thirdUserInfoService.getThirdUserInfosByUserId(userId);
        map.put("thirdUserInfos", thirdUserInfoList);
        return R.ok(map);
    }

    @GetMapping("/third/unBinding")
    public R<?> thirdUnBinding(HttpServletRequest request, @RequestParam Long kid) {
        Map<String, Object> map = new HashMap<>();
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        boolean result = thirdUserInfoService.removeById(kid);
        logger.info("删除第三方用户信息成功: {},{}", result, kid);
        List<ThirdUserInfoVO> thirdUserInfoList = thirdUserInfoService.getThirdUserInfosByUserId(userId);
        map.put("thirdUserInfos", thirdUserInfoList);
        return R.ok(map);
    }

    @PostMapping("/third/getThirdUserInfo/{userId}")
    public R<List<ThirdUserInfoVO>> getThirdUserInfo(@PathVariable Long userId) {
        List<ThirdUserInfoVO> thirdUserInfoList = thirdUserInfoService.getThirdUserInfosByUserId(userId);
        return R.ok(thirdUserInfoList);
    }


    @PostMapping("/saveUserInfo")
    public R<Object> saveUserInfo(HttpServletRequest request, @RequestBody SaveUserInfoVO saveUserInfoVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        saveUserInfoVO.setId(loginUser.getKid());
        // 判断是不是android手机上传上来的性别需进行转换
        if (saveUserInfoVO.getNewSex() == null && saveUserInfoVO.getSex() != null) {
            if (loginUser.getAppSystem()!=null && loginUser.getAppSystem() == 0){
                saveUserInfoVO.setNewSex(saveUserInfoVO.getSex() == 0 ? (byte) 1 : (saveUserInfoVO.getSex() == 1 ? (byte) 0 : (byte) 2));
            }else{
                saveUserInfoVO.setNewSex(saveUserInfoVO.getSex());
            }
        }
        boolean result = userInfoService.saveUserInfo(saveUserInfoVO);
        if (!result) {
            return R.fail("保存失败");
        }
        // 如果userInfoVO里的password不为空，则说明更改了密码，需要重新登录
        if (saveUserInfoVO.getPassword() != null && !saveUserInfoVO.getPassword().equals(loginUser.getPassword())) {
            tokenService.delLoginUserByUserkey(loginUser.getUserKey());
        } else {
            UserInfoVO entity = userInfoService.getUserInfo(loginUser.getKid());
            LoginUserInfo refreshLoginUser = new LoginUserInfo();
            BeanUtil.copyProperties(entity, refreshLoginUser);
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


        SaveUserInfoVO saveUserInfoVO = new SaveUserInfoVO().setId(loginUser.getKid()).setEmail(email);
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

        SaveUserInfoVO saveUserInfoVO = new SaveUserInfoVO().setId(loginUser.getKid()).setEmail(newEmail);
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
        List<IconVO> appList;
        List<Icon> icons =  iconService.list();
        appList = icons.stream().map(icon -> {
            IconVO iconVO = new IconVO();
            BeanUtil.copyProperties(icon, iconVO);
            return iconVO;
        }).collect(Collectors.toList());
        map.put("appList", appList);
        return R.ok(map);
    }
}
