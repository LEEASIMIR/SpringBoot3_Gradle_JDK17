package com.springboot.template.business.sample.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.template.common.custom.valid.annotation.ValidGlobalPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SampleSaveDto {

    @NotBlank(message = "exception.valid.not.blank")
    @Size(max = 50, message = "exception.valid.too.long")
    private String name;

    @NotBlank(message = "exception.valid.not.blank")
    @ValidGlobalPhoneNumber(message = "exception.valid.unknown.phone")
    private String phone;

}
