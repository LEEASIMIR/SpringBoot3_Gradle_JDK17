package com.springboot.template.common.model;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Getter
@Setter
public class ApiResponseEntity<T> extends ResponseEntity<T> {

    public ApiResponseEntity(HttpStatusCode status) {
        super(status);
    }

    public ApiResponseEntity(T body, HttpStatusCode status) {
        super(body, status);
    }

    public ApiResponseEntity(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public ApiResponseEntity(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public ApiResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers, statusCode);
    }

    public static <T> ApiResponseEntity<T> ok(T body) {
        return new ApiResponseEntity<>(body, HttpStatus.OK);
    }

    public static ApiResponseEntity<String> unauthorized(String message) {
        return new ApiResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ApiResponseEntity<T> other(HttpStatus status, T body) {
        return new ApiResponseEntity<>(body, status);
    }

    public static <T> ApiResponseEntity<T> error(T body) {
        return new ApiResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
