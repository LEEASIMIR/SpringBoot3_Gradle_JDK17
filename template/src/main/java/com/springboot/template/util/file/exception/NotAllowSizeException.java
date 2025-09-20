package com.springboot.template.util.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class NotAllowSizeException extends FileUploadException {
    public NotAllowSizeException(String message) {
        super(message);
    }
}
