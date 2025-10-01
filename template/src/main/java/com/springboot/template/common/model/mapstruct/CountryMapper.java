package com.springboot.template.common.model.mapstruct;

import com.springboot.template.common.model.data.entity.CountryEntity;
import com.springboot.template.common.model.dto.CountryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    List<CountryDto> toDto(List<CountryEntity> list);
    CountryDto toDto(CountryEntity entity);
    CountryEntity toEntity(CountryDto dto);
}
