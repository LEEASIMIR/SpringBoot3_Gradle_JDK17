package com.springboot.template.global.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.template.global.model.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalApiController {

    @GetMapping("/info")
    public ApiResponseEntity<Map<String, String>> info() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "자바 백엔드 템플릿");
        return ApiResponseEntity.ok(map);
    }

}
