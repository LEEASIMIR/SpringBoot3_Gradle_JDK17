package com.springboot.template.common.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
public class ApiResponseEntity<T> extends ResponseEntity<T> {

    public ApiResponseEntity(HttpStatusCode status) {
        super(status);
    }

    public ApiResponseEntity(T body, @Nonnull HttpStatusCode status) {
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

    public @Nonnull static <T> ApiResponseEntity<T> ok(T body) {
        return new ApiResponseEntity<>(body, HttpStatus.OK);
    }

    public @Nonnull static ApiResponseEntity<String> unauthorized(@Nonnull String message) {
        return new ApiResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    public @Nonnull static <T> ApiResponseEntity<T> other(@Nonnull HttpStatus status, T body) {
        return new ApiResponseEntity<>(body, status);
    }

    public @Nonnull static <T> ApiResponseEntity<T> error(@Nonnull T body) {
        return new ApiResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
