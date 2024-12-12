package com.kieslect.auth.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-06-14
 */
@Data
public class ThirdUserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 第三方token
     */
    private String thirdToken;

      /**
     * 第三方token类型，0：google，1：facebook，2：apple，3：wechat，4：Twitter，5：strava
     */
    private Byte thirdTokenType;

      /**
     * 第三方token状态
     */
    private Byte thirdTokenStatus;


    private String thirdId;

    private String email;

    private String verifiedEmail;

    private String name;

    private String firstName;

    private String lastName;

    private String middleName;

    private String birthday;

    private String gender;

    private String pictureUrl;

    private String profileLink;

    private String thirdUpdatedTime;
}
