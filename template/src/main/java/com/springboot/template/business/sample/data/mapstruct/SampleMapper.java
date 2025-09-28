package com.springboot.template.business.sample.data.mapstruct;

import com.springboot.template.business.sample.data.database.entity.SampleEntity;
import com.springboot.template.business.sample.data.dto.SampleDto;
import com.springboot.template.business.sample.data.dto.SampleSaveDto;
import com.springboot.template.business.sample.data.dto.SampleUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleMapper {
    SampleDto toDto(SampleEntity entity);
    List<SampleDto> toDto(List<SampleEntity> entities);
    SampleEntity toEntity(SampleSaveDto dto);
    SampleEntity toEntity(SampleUpdateDto dto);
    SampleEntity toEntity(SampleDto dto);

    // null이 아닌 필드만 업데이트
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SampleUpdateDto dto, @MappingTarget SampleEntity entity);

    default Page<SampleDto> toDtoPage(Page<SampleEntity> entityPage) {
        List<SampleDto> dtoList = toDto(entityPage.getContent());
        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }
}
