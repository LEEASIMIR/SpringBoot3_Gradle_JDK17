package com.springboot.template.business.member.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberWithDrawDto {

    @NotNull
    private Long id;
}
