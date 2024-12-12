package com.kieslect.auth.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StravaRefreshTokenVO {
    @JsonProperty("token_type")
    private String tokenType; // token_type

    @JsonProperty("access_token")
    private String accessToken; // access_token

    @JsonProperty("expires_at")
    private long expiresAt; // expires_at

    @JsonProperty("expires_in")
    private int expiresIn; // expires_in

    @JsonProperty("refresh_token")
    private String refreshToken; // refresh_token
}
