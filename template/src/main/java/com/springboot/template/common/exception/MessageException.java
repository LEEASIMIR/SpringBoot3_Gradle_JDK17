package com.springboot.template.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
public class MessageException extends Exception {

    private final String message;
    private final String[] argKeys;

    public MessageException(String messageKey) {
        this.message = Objects.requireNonNull(messageKey, "메시지 키는 null일 수 없습니다.");
        this.argKeys = new String[]{};
    }
    public MessageException(String messageKey, String[] args) {
        this.message = Objects.requireNonNull(messageKey, "메시지 키는 null일 수 없습니다.");
        this.argKeys = args;
    }
}
