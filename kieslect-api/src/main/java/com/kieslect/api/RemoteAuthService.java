package com.kieslect.api;

import com.kieslect.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteAuthService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteAuthService {
    @GetMapping("/auth/strava/getRefreshToken")
    String getRefreshToken(@RequestParam("refreshToken") String refreshToken);
}
