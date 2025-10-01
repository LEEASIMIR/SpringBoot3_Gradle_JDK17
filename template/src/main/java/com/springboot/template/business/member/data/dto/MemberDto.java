package com.springboot.template.business.member.data.dto;

import com.springboot.template.common.model.dto.CountryDto;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class MemberDto {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private Instant lastLoginDate;
    private Instant lastChangePasswordDate;
    private List<RoleDto> roles;
    private List<CountryDto> countries;
}
