package com.springboot.template.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.types.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class PageSortDto {
    private Order sortDirection;
    private String sortColumn;
}
