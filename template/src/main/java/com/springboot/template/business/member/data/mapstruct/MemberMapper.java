package com.springboot.template.business.member.data.mapstruct;

import com.springboot.template.business.member.data.database.entity.MemberEntity;
import com.springboot.template.business.member.data.dto.MemberDto;
import com.springboot.template.business.member.data.dto.MemberSaveDto;
import com.springboot.template.business.member.data.dto.MemberUpdateDto;
import com.springboot.template.common.model.data.entity.CountryEntity;
import com.springboot.template.common.model.dto.CountryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    List<MemberDto> toDto(List<MemberEntity> list);
    MemberDto toDto(MemberEntity entity);

    MemberEntity toEntity(MemberSaveDto dto);
    MemberEntity toEntity(MemberUpdateDto dto);

    CountryEntity map(CountryDto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MemberEntity updateFromDto(MemberUpdateDto dto, @MappingTarget MemberEntity entity);
}
