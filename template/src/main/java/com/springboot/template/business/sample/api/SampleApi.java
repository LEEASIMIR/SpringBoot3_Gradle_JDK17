package com.springboot.template.business.sample.api;

import com.springboot.template.business.sample.data.dto.*;
import com.springboot.template.business.sample.service.SampleService;
import com.springboot.template.common.model.ApiResponseEntity;
import com.springboot.template.common.model.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sample/api")
public class SampleApi {

    private final SampleService sampleService;

    @GetMapping("/querydsl")
    public ApiResponseEntity<ResponseDto<SampleDto>> indexquerydsl(@RequestBody SampleSelectDto dto) throws Exception {
        return ApiResponseEntity.ok(sampleService.selectFromQueryDsl(dto));
    }

    @PostMapping("/save")
    public ApiResponseEntity<SampleDto> save(@RequestBody @Valid SampleSaveDto dto) {
        return ApiResponseEntity.ok(sampleService.save(dto));
    }

    @PostMapping("/update")
    public ApiResponseEntity<SampleDto> update(@RequestBody @Valid SampleUpdateDto dto) {
        return ApiResponseEntity.ok(sampleService.update(dto));
    }

}
