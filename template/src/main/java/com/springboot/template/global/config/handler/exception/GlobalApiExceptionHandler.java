package com.springboot.template.global.config.handler.exception;

import com.springboot.template.global.model.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API, @RestController 대상 Exception 핸들러
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseEntity<String> handleException(Exception e) {
        return ApiResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "UNKNOWN ERROR", e.getMessage());
    }

}
