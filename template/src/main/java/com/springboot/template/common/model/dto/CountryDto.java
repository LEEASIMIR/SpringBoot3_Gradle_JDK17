package com.springboot.template.common.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDto {
    private String id;
    private String countryName;
    private String isoCode;
    private String countryCode;
}
