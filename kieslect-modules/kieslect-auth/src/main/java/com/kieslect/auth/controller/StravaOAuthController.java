package com.kieslect.auth.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kieslect.auth.converter.ThirdUserInfoConverter;
import com.kieslect.auth.domain.StravaTokenInfo;
import com.kieslect.auth.domain.vo.CodeRequestVO;
import com.kieslect.auth.service.IStravaTokenInfoService;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Strava OAuth 登录控制器
 */
@RestController
@RequestMapping("/strava")
public class StravaOAuthController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StravaOAuthController.class);
    // 将 clientId 和 clientSecret 作为常量配置
    private static final String STRAVA_OAUTH_URL = "https://www.strava.com/oauth/token";
    private static final String CLIENT_ID = "129194";
    private static final String CLIENT_SECRET = "fd18b16ec45236ebd80db866aa2961688a2df842";
    private static final String GRANT_TYPE = "authorization_code";

    @Autowired
    IStravaTokenInfoService stravaTokenInfoService;

    /**
     * 提供获取 Strava Access Token 的接口
     * {"code":200,"msg":null,"data":{"token_type":"Bearer","expires_at":1731170888,"expires_in":21402,"refresh_token":"a4397a05ac10f150b5dabf629c78321235b23f30","access_token":"6905609a9d0bf8fb182ac1a2ba28ea9ef9930554","athlete":{"id":140802912,"resource_state":2,"firstname":"Kieslect","lastname":"Kieslect","city":"Phoenix","state":"Arizona","country":"United States","sex":"M","premium":false,"summit":false,"created_at":"2024-06-25T11:33:12Z","updated_at":"2024-11-09T09:38:47Z","badge_type_id":0,"profile_medium":"https://lh3.googleusercontent.com/a/ACg8ocJ7iZFknQ-lbWYG4hFYpR-n_XbxgE3XY2OwWn4qJsXxId_kJw=s96-c","profile":"https://lh3.googleusercontent.com/a/ACg8ocJ7iZFknQ-lbWYG4hFYpR-n_XbxgE3XY2OwWn4qJsXxId_kJw=s96-c"}}}
     *
     * @param codeRequestVO 包含授权码的登录信息
     * @return 返回 Strava OAuth 的响应信息（包含 Access Token 或错误信息）
     */
    @PostMapping("/getAccessToken")
    public R<?> getAccessToken(@RequestBody CodeRequestVO codeRequestVO) {
        try {
            // 获取授权码
            String code = codeRequestVO.getCode();

            // 调用获取 Access Token 的方法
            String response = fetchAccessToken(code);

            // 假设返回一个统一的响应格式（R 是你自定义的响应类）
            if (response.contains("access_token")) {
                // 如果响应中包含 access_token，先保存第三方Token信息，再返回成功信息
                stravaTokenInfoService.insertOrUpdateStravaTokenInfo(convertJsonToEntity(response));
                return R.ok(ThirdUserInfoConverter.convertJsonToEntity(JSONUtil.parseObj(response)));
            } else {
                // 如果没有 access_token，返回失败信息
                return R.fail(ResponseCodeEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("Strava OAuth 获取失败，错误信息: {}", e);
            return R.fail(ResponseCodeEnum.FAIL);
        }
    }

    @GetMapping("/getRefreshToken")
    public String getRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        return stravaTokenInfoService.getRefreshToken(refreshToken);
    }

    /**
     * 调用 Strava API 获取 Access Token
     *
     * @param code 授权码
     * @return 返回 Strava 的 OAuth 响应信息（包括 Access Token 或错误信息）
     */
    private String fetchAccessToken(String code) {

        // 使用 Hutool 发起 POST 请求，向 Strava 发送请求获取 Access Token
        HttpResponse response = HttpRequest.post(STRAVA_OAUTH_URL)
                .form("client_id", CLIENT_ID)
                .form("client_secret", CLIENT_SECRET)
                .form("code", code)
                .form("grant_type", GRANT_TYPE)
                .execute();

        // 返回响应的 body 内容（Access Token 或错误信息）
        return response.body();
    }

    // 将 JSON 转换为 StravaTokenInfo 实体类
    private StravaTokenInfo convertJsonToEntity(String json) {
        JSONObject jsonObject = new JSONObject(json);

        // 获取 Token 信息
        String tokenType = jsonObject.getStr("token_type");
        Long expiresAt = jsonObject.getLong("expires_at");
        Integer expiresIn = jsonObject.getInt("expires_in");
        String refreshToken = jsonObject.getStr("refresh_token");
        String accessToken = jsonObject.getStr("access_token");

        // 获取 Athlete 信息
        JSONObject athlete = jsonObject.getJSONObject("athlete");
        Long athleteId = athlete.getLong("id");
        Integer resourceState = athlete.getInt("resource_state");
        String firstname = athlete.getStr("firstname");
        String lastname = athlete.getStr("lastname");
        String city = athlete.getStr("city");
        String state = athlete.getStr("state");
        String country = athlete.getStr("country");
        String sex = athlete.getStr("sex");
        Boolean premium = athlete.getBool("premium");
        Boolean summit = athlete.getBool("summit");
        String profileMedium = athlete.getStr("profile_medium");
        String profile = athlete.getStr("profile");
        String createdAtAthlete = athlete.getStr("created_at");
        String updatedAtAthlete = athlete.getStr("updated_at");

        // 创建 StravaTokenInfo 实体
        StravaTokenInfo stravaTokenInfo = new StravaTokenInfo();
        stravaTokenInfo.setTokenType(tokenType);
        stravaTokenInfo.setExpiresAt(expiresAt);
        stravaTokenInfo.setExpiresIn(expiresIn);
        stravaTokenInfo.setRefreshToken(refreshToken);
        stravaTokenInfo.setAccessToken(accessToken);
        stravaTokenInfo.setAthleteId(athleteId);
        stravaTokenInfo.setResourceState(resourceState);
        stravaTokenInfo.setFirstname(firstname);
        stravaTokenInfo.setLastname(lastname);
        stravaTokenInfo.setCity(city);
        stravaTokenInfo.setState(state);
        stravaTokenInfo.setCountry(country);
        stravaTokenInfo.setSex(sex);
        stravaTokenInfo.setPremium((byte) (premium ? 1 : 0));
        stravaTokenInfo.setSummit((byte) (summit ? 1 : 0));
        stravaTokenInfo.setProfileMedium(profileMedium);
        stravaTokenInfo.setProfile(profile);
        stravaTokenInfo.setThirdCreatedAt(Instant.parse(createdAtAthlete).getEpochSecond());
        stravaTokenInfo.setThirdUpdatedAt(Instant.parse(updatedAtAthlete).getEpochSecond());
        stravaTokenInfo.setCreateTime(Instant.now().getEpochSecond());
        stravaTokenInfo.setUpdateTime(Instant.now().getEpochSecond());

        return stravaTokenInfo;
    }
}
