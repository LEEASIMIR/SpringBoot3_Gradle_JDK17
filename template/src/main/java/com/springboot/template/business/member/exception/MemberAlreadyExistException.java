package com.springboot.template.business.member.exception;

public class MemberAlreadyExistException extends RuntimeException {
    public MemberAlreadyExistException(String message) {
        super(message);
    }
}
