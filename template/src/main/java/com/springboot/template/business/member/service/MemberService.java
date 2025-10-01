package com.springboot.template.business.member.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.template.business.member.data.database.entity.MemberEntity;
import com.springboot.template.business.member.data.database.entity.QMemberEntity;
import com.springboot.template.business.member.data.database.repository.MemberRepository;
import com.springboot.template.business.member.data.database.repository.RoleRepository;
import com.springboot.template.business.member.data.dto.*;
import com.springboot.template.business.member.data.mapstruct.MemberMapper;
import com.springboot.template.business.member.exception.MemberAlreadyExistException;
import com.springboot.template.business.member.exception.MemberNotExistException;
import com.springboot.template.common.model.data.entity.CountryEntity;
import com.springboot.template.common.model.data.repository.CountryRepository;
import com.springboot.template.common.model.dto.ResponseDto;
import com.springboot.template.common.querydsl.util.QueryDslUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final JPAQueryFactory jpaQueryFactory;

    QMemberEntity qMember = QMemberEntity.memberEntity;

    public MemberDto get(String username) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findByUsername(username);
        if(memberEntityOpt.isEmpty()) throw new MemberNotExistException("member not exist");
        return memberMapper.toDto(memberEntityOpt.get());
    }

    public Optional<MemberEntity> getDetachedMemberEntity(String username) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findByUsername(username);
        if(memberEntityOpt.isEmpty()) throw new MemberNotExistException("member not exist");
        return memberEntityOpt;
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto update(MemberSaveDto dto, List<String> role) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findByUsername(dto.getUsername());
        if(memberEntityOpt.isEmpty()) throw new MemberNotExistException("member not exist");
        //TODO 전처리
        return this.save(dto, role);
    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto create(MemberSaveDto dto, List<String> role) {
        Optional<MemberEntity> memberEntityOpt = memberRepository.findByUsername(dto.getUsername());
        if(memberEntityOpt.isPresent()) throw new MemberAlreadyExistException("member already exist");
        //TODO 전처리
        return this.save(dto, role);
    }

    private MemberDto save(MemberSaveDto dto, List<String> role) {

        log.info("{}", dto);

        MemberEntity memberEntity = memberMapper.toEntity(dto);
        memberEntity = memberRepository.save(memberEntity);
        memberEntity.setEncryptedPassword(dto.getPassword());
        memberEntity.setRoles(roleRepository.findAllById(role));
        memberEntity.setCountries(this.getCountries(dto.getCountryIds()));

        return memberMapper.toDto(memberEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean withDraw(MemberWithDrawDto dto) {
        //TODO 전처리
        return this.delete(dto.getId());
    }

    private boolean delete(Long id) {
        memberRepository.deleteById(id);
        return true;
    }

    private List<CountryEntity> getCountries(List<String> ids) {
        return countryRepository.findAllById(ids);
    }

    public ResponseDto<MemberDto> select(MemberSelectDto dto) {

        //조건
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder
                .and(QueryDslUtils.between(dto.getCreateBegin(), qMember.createDate, dto.getCreateEnd()))
                .and(QueryDslUtils.between(dto.getUpdateBegin(), qMember.updateDate, dto.getUpdateEnd()))
                .and(QueryDslUtils.containsColumns(List.of(qMember.name, qMember.phone), dto.getKeyword()));

        //컨텐츠수
        Long total = jpaQueryFactory.select(qMember.count())
            .from(qMember)
            .where(whereBuilder).fetchOne();

        //컨텐츠
        List<MemberEntity> list = jpaQueryFactory.select(qMember)
                .from(qMember)
                .where(whereBuilder)
                .orderBy(QueryDslUtils.getOrderSpecs(MemberEntity.class, "memberEntity", dto.getSorts()))
                .offset(dto.getPageNumber() * dto.getPageSize())
                .limit(dto.getPageSize())
                .fetch();

        List<MemberDto> listDto = memberMapper.toDto(list);

        return new ResponseDto<>(dto.getPageNumber(), dto.getPageSize(), total, dto.getSorts(), listDto);
    }
}
