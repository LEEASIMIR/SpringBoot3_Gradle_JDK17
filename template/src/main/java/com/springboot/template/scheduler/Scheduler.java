package com.springboot.template.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Scheduler {
    @Scheduled(fixedRate = 300)
    public void runTaskFixedRate() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory(); // JVM이 사용하는 총 메모리
        long freeMemory = runtime.freeMemory();   // 사용 가능한 메모리
        long usedMemory = totalMemory - freeMemory; // 사용 중인 메모리

        double memoryUsagePercentage = (double) usedMemory / totalMemory * 100;
//        log.info("메모리 사용률: {}%", memoryUsagePercentage);
    }
}
