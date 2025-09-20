package com.springboot.template.util.file.vo;


import com.springboot.template.util.file.base.FileType;

public record UploadFileInfoVO(String fileName, String physicsFilePath, String physicsFileName, FileType fileType,
                               long sizeByte) {
}
