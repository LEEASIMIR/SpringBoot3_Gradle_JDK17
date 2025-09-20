package com.springboot.template.util.file.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownloadReqDto {
    private String returnFileName;
    private String filePath;
}
