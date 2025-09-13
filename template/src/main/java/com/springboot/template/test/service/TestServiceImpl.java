package com.springboot.template.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class TestServiceImpl {
    public String throwsError() {
        Integer integer = 1 / 0;
        return "hello world";
    }

    public void uploadFile(MultipartFile file) {

    }
}
