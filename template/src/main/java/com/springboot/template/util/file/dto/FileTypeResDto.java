package com.springboot.template.util.file.dto;

import com.springboot.template.util.file.FileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileTypeResDto {
    private FileType fileType;
    private boolean isAllow;
}
