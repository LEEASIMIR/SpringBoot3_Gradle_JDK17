package com.springboot.template.common.custom.exception;

import com.springboot.template.common.model.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API, @RestController 대상 Exception 핸들러
 * business 패키지 에서 필요하면 추가해서 쓰면 동작함
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionHandlerRestApi {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ApiResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponseEntity.error(e.getMessage());
    }

    @ExceptionHandler(MessageException.class)
    public ApiResponseEntity<String> handleMessageException(MessageException e) {
        String message = this.getMessage(e.getMessage(), e.getArgKeys());
        log.error(message, e);
        return ApiResponseEntity.error(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        String message = this.getMessage("exception.auth.access.denied");
        log.error(message, e);
        return ApiResponseEntity.other(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

        String message;
        if (e.getMessage().contains("Duplicate entry")) {
            message = this.getMessage("exception.database.save.already.exist");
        } else if (e.getMessage().contains("cannot be null")) {
            message = this.getMessage("exception.database.save.not.allow.null");
        } else {
            message = this.getMessage("exception.database.save.unknown");
        }

        log.error(message, e);
        return ApiResponseEntity.error(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseEntity<List<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Map<String, String>> errorList = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errorList.add(Map.of("key", fieldName, "message", this.getMessage(error.getDefaultMessage())));
        });
        return ApiResponseEntity.error(errorList);
    }

    private String getMessage(String key) {
        return this.getMessage(key, null);
    }

    private String getMessage(String key, String[] args) {
        String message;
        try {
            message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException noSuch) {
            log.error("NoSuchMessageException {}", noSuch.getMessage());
            message = key;
        }
        return message;
    }

}
