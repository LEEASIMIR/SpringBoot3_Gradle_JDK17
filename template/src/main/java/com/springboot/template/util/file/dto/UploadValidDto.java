package com.springboot.template.util.file.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 청크 업로드 Dto
 * @author 이봉용
 * @date 25. 9. 20.
 */
@Getter
@Setter
public class UploadValidDto {
    private String uploadPath;
    private String fileName;
    private long fileSize;
    private String fileType;
}
