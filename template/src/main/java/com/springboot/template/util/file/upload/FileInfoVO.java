package com.springboot.template.util.file.upload;


import com.springboot.template.util.file.FileType;

public record FileInfoVO(String fileName, String physicsFilePath, String physicsFileName, FileType fileType,
                         long sizeByte) {
}
