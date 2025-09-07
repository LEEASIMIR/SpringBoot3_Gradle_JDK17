package com.springboot.template.business.sample.api;

import com.springboot.template.ApiRoutes;
import com.springboot.template.global.model.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiRoutes.SAMPLE_API_URL)
public class SampleApiController {

    @GetMapping
    public ApiResponseEntity<String> index() throws Exception {
        return ApiResponseEntity.ok("Hello");
    }

    @GetMapping("/get/{id}")
    public ApiResponseEntity<String> getById(@PathVariable String id) throws Exception {
        return ApiResponseEntity.ok(id);
    }

//    @GetMapping("/get")
//    public ApiResponseEntity<String> getById(@RequestParam Map id) throws Exception {
//        return ApiResponseEntity.ok(id);
//    }

}
