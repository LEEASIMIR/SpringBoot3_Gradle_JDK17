package com.springboot.template.business.sample.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.template.common.model.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SampleSelectDto extends PageDto {
    private String keyword;
    private List<Long> ids;
    private Instant createBegin;//javascript:new Date().toIOSString()
    private Instant createEnd;//javascript:new Date().toIOSString()
    private Instant updateBegin;//javascript:new Date().toIOSString()
    private Instant updateEnd;//javascript:new Date().toIOSString()
}
