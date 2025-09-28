package com.springboot.template.business.sample.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SampleDto {
    private Long id;
    private String name;
    private String phone;
    private Date createDate;
    private Date updateDate;
}
