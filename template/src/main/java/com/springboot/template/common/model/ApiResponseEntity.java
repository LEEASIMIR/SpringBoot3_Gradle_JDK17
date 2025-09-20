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
public class ApiResponseEntity<T> {

    private int status;
    private String message;
    private T body;

    public static <T> ApiResponseEntity<T> ok(HttpServletResponse response, T body) {
        ApiResponseEntity<T> entity = new ApiResponseEntity<>();
        response.setStatus(HttpStatus.OK.value());
        entity.setStatus(HttpStatus.OK.value());
        entity.setMessage("SUCCESS");
        entity.setBody(body);
        return entity;
    }

    public static <T> ApiResponseEntity<T> other(HttpServletResponse response, HttpStatus status, String message, T body) {
        ApiResponseEntity<T> entity = new ApiResponseEntity<>();
        response.setStatus(status.value());
        entity.setStatus(status.value());
        entity.setMessage(message);
        entity.setBody(body);
        return entity;
    }

    public static <T> ApiResponseEntity<T> error(HttpServletResponse response, String message, T body) {
        ApiResponseEntity<T> entity = new ApiResponseEntity<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        entity.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        entity.setMessage(message);
        entity.setBody(body);
        return entity;
    }
}
