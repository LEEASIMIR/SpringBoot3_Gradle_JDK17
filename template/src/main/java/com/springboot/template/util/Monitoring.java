package com.springboot.template.util;

public class Monitoring {

    public static double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory(); // JVM이 사용하는 총 메모리
        long freeMemory = runtime.freeMemory();   // 사용 가능한 메모리
        long usedMemory = totalMemory - freeMemory; // 사용 중인 메모리

        return (double) usedMemory / totalMemory * 100;
    }
}
