package com.springboot.template.business.sample.controller.api;

import com.springboot.template.business.sample.service.SampleService;
import com.springboot.template.common.model.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sample/api")
public class SampleApiController {

    private final SampleService sampleService;

    @GetMapping
    public ApiResponseEntity<String> index() throws Exception {
        return ApiResponseEntity.ok(sampleService.sampleTest());
    }

}
