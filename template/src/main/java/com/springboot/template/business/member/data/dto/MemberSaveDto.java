package com.springboot.template.business.member.data.dto;

import com.springboot.template.common.model.dto.CountryDto;
import com.springboot.template.common.rule.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
public class MemberSaveDto {

    private Long id;

    @NotNull(message = "not_null")
    @NotBlank(message = "not_blank")
    @Size(min = 3, max = 50, message = "3-50_size")
    private String username;

    @NotNull(message = "not_null")
    @NotBlank(message = "not_blank")
    @Size(min = 3, max = 50, message = "3-50_size")
    private String password;

    @NotNull(message = "not_null")
    @NotBlank(message = "not_blank")
    @Size(min = 3, max = 20, message = "3-20_size")
    private String name;

    @NotNull(message = "not_null")
    @NotBlank(message = "not_blank")
    @Size(min = 10, max = 11, message = "10-11_size")
    private String phone;

    @NotNull(message = "not_null")
    private List<CountryDto> countries;

    @NotNull(message = "not_null")
    @NotBlank(message = "not_blank")
    @Email(message = "not_email")
    private String email;

    public List<String> getCountryIds() {
        return this.getCountries().stream().map(CountryDto::getId).collect(Collectors.toList());
    }

}
