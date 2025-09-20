package com.springboot.template.util.file.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 청크 업로드 Dto
 * @author 이봉용
 * @date 25. 9. 20.
 */
@Getter
@Setter
public class UploadChunkDto {
    /**
     * 청크파일
     */
    private MultipartFile chunk;
    /**
     * 청크를 보관할 임시 디렉토리명 유니크하게
     */
    private String uniqueTempDirName;
    /**
     * 원본파일명
     */
    private String originFileName;
    /**
     * 청크 업로드 할 Index
     */
    private int currentIndex;
    /**
     * 모든 청크 개수
     */
    private int totalChunkCnt;
}
