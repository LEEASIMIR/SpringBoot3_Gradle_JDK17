package com.springboot.template.business.member.data.dto;

import com.springboot.template.common.model.dto.CountryDto;
import com.springboot.template.common.rule.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberUpdateDto {

    @NotNull
    private Long id;

    @Size(min = 3, max = 50, message = "3-50_size")
    private String username;

    @Size(min = 3, max = 50, message = "3-50_size")
    private String password;

    @Size(min = 3, max = 20, message = "3-20_size")
    private String name;

    @Size(min = 10, max = 11, message = "10-11_size")
    private String phone;

    private List<CountryDto> countries;

    @Email(message = "not_email")
    private String email;

    public List<String> getCountryIds() {
        return this.getCountries().stream().map(CountryDto::getId).collect(Collectors.toList());
    }

}
