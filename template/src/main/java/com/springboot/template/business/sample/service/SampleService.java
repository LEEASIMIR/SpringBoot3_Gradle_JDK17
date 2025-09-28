package com.springboot.template.business.sample.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.template.business.sample.data.database.entity.QSampleEntity;
import com.springboot.template.business.sample.data.database.entity.SampleEntity;
import com.springboot.template.business.sample.data.database.repository.SampleRepository;
import com.springboot.template.common.querydsl.util.QueryDslUtils;
import com.springboot.template.business.sample.data.dto.*;
import com.springboot.template.business.sample.data.mapstruct.SampleMapper;
import com.springboot.template.common.model.dto.PageSortDto;
import com.springboot.template.common.model.dto.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SampleService {

    private final SampleMapper sampleMapper;
    private final SampleRepository sampleRepository;
    private final JPAQueryFactory jpaQueryFactory;

    QSampleEntity qSample = QSampleEntity.sampleEntity;
    //위와 같은 역할이지만 String 으로 Q 객체의 컬럼을 가져오고 싶을때 사용
    PathBuilder<SampleEntity> entityPath = new PathBuilder<>(SampleEntity.class, "sampleEntity");

    public String sampleTest() {

        SampleEntity sampleEntity = new SampleEntity();

        log.info("SampleService.sampleTest");
        return "sampleTest";
    }

    @Transactional(rollbackFor = Exception.class)
    public SampleDto save(SampleSaveDto dto) {

        SampleEntity sampleEntity = sampleMapper.toEntity(dto);

        sampleRepository.saveAndFlush(sampleEntity);

        return sampleMapper.toDto(sampleEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public SampleDto update(SampleUpdateDto dto) {

        SampleEntity sampleEntity = sampleRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("not found"));

        sampleMapper.updateFromDto(dto, sampleEntity);

        return sampleMapper.toDto(sampleEntity);
    }

    public ResponseDto<SampleDto> selectFromQueryDsl(SampleSelectDto dto) {

        //조건
        BooleanBuilder whereBuilder = new BooleanBuilder();
        whereBuilder
                .and(QueryDslUtils.between(dto.getCreateBegin(), qSample.createDate, dto.getCreateEnd()))
                .and(QueryDslUtils.between(dto.getUpdateBegin(), qSample.updateDate, dto.getUpdateEnd()))
                .and(QueryDslUtils.containsColumns(List.of(qSample.name, qSample.phone), dto.getKeyword()))
                .and(QueryDslUtils.in(qSample.id, dto.getIds()));

        OrderSpecifier<?>[] orderSpecs = this.getOrderSpecs(dto.getSorts());

        //컨텐츠수
        long total = 0;
        Long fetchOne = jpaQueryFactory.select(qSample.count())
            .from(qSample)
            .where(whereBuilder).fetchOne();

        total = fetchOne == null ? 0 : fetchOne;

        //컨텐츠
        List<SampleEntity> list = jpaQueryFactory.select(qSample)
                .from(qSample)
                .where(whereBuilder)
                .orderBy(orderSpecs)
                .offset(dto.getPageNumber() * dto.getPageSize())
                .limit(dto.getPageSize())
                .fetch();

        List<SampleDto> listDto = sampleMapper.toDto(list);

        return new ResponseDto<>(dto.getPageNumber(), dto.getPageSize(), total, dto.getSorts(), listDto);
    }

    private BooleanExpression idsInCond(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return qSample.id.in(ids);
    }

    private OrderSpecifier<?>[] getOrderSpecs(List<PageSortDto> sorts) {
        List<OrderSpecifier<?>> result = new ArrayList<>();
        for(PageSortDto dto: sorts) {
            log.info("{}", dto);
            if (dto.getSortColumn() != null && !dto.getSortColumn().isEmpty())
                result.add(new OrderSpecifier<>(dto.getSortDirection(), entityPath.getString(dto.getSortColumn())));
            else
                result.add(new OrderSpecifier<>(dto.getSortDirection(), entityPath.getString("createDate")));
        }

        return result.toArray(new OrderSpecifier<?>[0]);
    }
}
