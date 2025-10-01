package com.springboot.template.business.member.data.dto;

import com.springboot.template.common.model.dto.CountryDto;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * 응답으로 내려주거나 받는 용도로 사용 금지
 * @author 이봉용
 * @date 25. 10. 1.
 */
@Getter
@Builder
public class MemberPrivateDto {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Instant lastLoginDate;
    private Instant lastChangePasswordDate;
    private List<RoleDto> roles;
    private List<CountryDto> countries;
}
