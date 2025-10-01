package com.springboot.template.business.member.exception;

public class MemberNotExistException extends RuntimeException {
    public MemberNotExistException(String message) {
        super(message);
    }
}
