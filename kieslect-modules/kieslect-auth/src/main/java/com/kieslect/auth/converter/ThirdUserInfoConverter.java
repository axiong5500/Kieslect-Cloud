package com.kieslect.auth.converter;

import cn.hutool.json.JSONObject;
import com.kieslect.auth.domain.vo.ThirdUserInfoVO;

public class ThirdUserInfoConverter {
    public static ThirdUserInfoVO convertJsonToEntity(JSONObject jsonData) {
        JSONObject data = jsonData;
        JSONObject athlete = jsonData.getJSONObject("athlete");

        ThirdUserInfoVO userInfo = new ThirdUserInfoVO();

        userInfo.setThirdToken(data.getStr("access_token"));


        userInfo.setThirdId(athlete.getStr("id"));
        userInfo.setName(athlete.getStr("firstname") + " " + athlete.getStr("lastname"));
        userInfo.setFirstName(athlete.getStr("firstname"));
        userInfo.setLastName(athlete.getStr("lastname"));

        userInfo.setPictureUrl(athlete.getStr("profile_medium"));
        userInfo.setProfileLink(athlete.getStr("profile"));
        userInfo.setThirdUpdatedTime(athlete.getStr("updated_at"));
        userInfo.setGender(athlete.getStr("sex"));

        return userInfo;
    }
}
