package com.springboot.template.common.exception;

import com.springboot.template.common.model.ApiResponseEntity;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ApiResponseEntity.error(attributes.getResponse(), e.getMessage(), "UNKNOWN ERROR");
    }

    @ExceptionHandler(MessageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseEntity<String> handleMessageException(MessageException e) {
        String message = "";
        try {
            message = messageSource.getMessage(e.getMessage(), e.getArgKeys(), LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException noSuch) {
            log.error("NoSuchMessageException {}", noSuch.getMessage());
            message = messageSource.getMessage("exception.unknown", null, LocaleContextHolder.getLocale());
        }
        log.error(message, e);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ApiResponseEntity.error(attributes.getResponse(), message, "");
    }

}
