package com.springboot.template.security.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
