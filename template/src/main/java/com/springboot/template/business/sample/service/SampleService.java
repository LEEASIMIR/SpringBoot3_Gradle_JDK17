package com.springboot.template.business.sample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleService {
    public String sampleTest() {
        log.info("SampleService.sampleTest");
        return "sampleTest";
    }
}
