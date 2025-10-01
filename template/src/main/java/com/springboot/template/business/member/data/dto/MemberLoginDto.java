package com.springboot.template.business.member.data.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginDto {
    private String username;
    private String password;
}
