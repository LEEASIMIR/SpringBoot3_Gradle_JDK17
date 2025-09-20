package com.springboot.template.util.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class NotAllowTypeException extends FileUploadException {
    public NotAllowTypeException(String message) {
        super(message);
    }
}
