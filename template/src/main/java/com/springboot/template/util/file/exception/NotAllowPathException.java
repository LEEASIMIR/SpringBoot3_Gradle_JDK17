package com.springboot.template.util.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class NotAllowPathException extends FileUploadException {
    public NotAllowPathException(String message) {
        super(message);
    }
}
