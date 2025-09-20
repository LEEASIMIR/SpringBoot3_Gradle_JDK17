package com.springboot.template.util.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class InvalidUploadChunkException extends FileUploadException {
    public InvalidUploadChunkException(String message) {
        super(message);
    }
}
