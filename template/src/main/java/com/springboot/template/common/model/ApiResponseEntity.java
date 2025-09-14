package com.springboot.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public static <T> ApiResponseEntity<T> ok(T body) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>();
        response.setStatus(200);
        response.setMessage("SUCCESS");
        response.setBody(body);
        return response;
    }

    public static <T> ApiResponseEntity<T> error(int code, String message, T body) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>();
        response.setStatus(code);
        response.setMessage(message);
        response.setBody(body);
        return response;
    }
}
