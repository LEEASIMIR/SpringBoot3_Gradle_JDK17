package com.springboot.template.util.file.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * 청크 업로드 Dto
 * @author 이봉용
 * @date 25. 9. 20.
 */
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

    public MultipartFile getChunk() {
        return chunk;
    }

    public void setChunk(MultipartFile chunk) {
        this.chunk = chunk;
    }

    public String getUniqueTempDirName() {
        return uniqueTempDirName;
    }

    public void setUniqueTempDirName(String uniqueTempDirName) {
        this.uniqueTempDirName = uniqueTempDirName;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getTotalChunkCnt() {
        return totalChunkCnt;
    }

    public void setTotalChunkCnt(int totalChunkCnt) {
        this.totalChunkCnt = totalChunkCnt;
    }
}
