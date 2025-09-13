package com.springboot.template.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseEntity<T> {
    private int code;
    private String message;
    private T body;

    public static <T> ApiResponseEntity<T> ok(T body) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>();
        response.setCode(200);
        response.setMessage("SUCCESS");
        response.setBody(Objects.requireNonNull(body, ""));
        return response;
    }

    public static <T> ApiResponseEntity<T> error(int code, String message, T body) {
        ApiResponseEntity<T> response = new ApiResponseEntity<>();
        response.setCode(code);
        response.setMessage(message);
        response.setBody(Objects.requireNonNull(body, ""));
        return response;
    }
}
