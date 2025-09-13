package com.springboot.template.util.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 파일 타입 종류 정의
 * @author 이봉용
 * @date 25. 9. 13.
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum FileType {

    //이미지
    UNKNOWN("unknown", "application/octet-stream"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    GIF("gif", "image/gif"),

    //영상
    MP4("mp4", "video/mp4"),
    AVI("avi", "video/x-msvideo"),
    MOV("mov", "video/quicktime"),
    WMV("wmv", "video/x-ms-wmv"),
    MKV("mkv", "video/x-matroska"),
    WEBM("webm", "video/webm"),

    // 문서
    PDF("pdf", "application/pdf"),
    DOC("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLS("xls", "application/vnd.ms-excel"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT("ppt", "application/vnd.ms-powerpoint"),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    TXT("txt", "text/plain"),
    CSV("csv", "text/csv"),
    JSON("json", "application/json"),
    XML("xml", "application/xml");

    private final String extension;
    private final String contentType;

    public static FileType getFileTypeByMultipartFile(MultipartFile file, boolean hardCheck) {
        String contentType = FileType.getContentType(file);
        String extension = FileType.getExtension(file);
        if(hardCheck) {
            return FileType.findByExtensionAndMimeType(extension, contentType);
        } else {
            return FileType.findByExtension(extension);
        }
    }

    public static String getExtension(MultipartFile file) {
        String extension = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return extension;
    }

    public static String getContentType(MultipartFile file) {
        return file.getContentType();
    }

    //하드 체크, 둘다 만족
    private static FileType findByExtensionAndMimeType(String fileExtension, String fileContentType) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.getExtension().equalsIgnoreCase(fileExtension) &&
                        type.getContentType().equalsIgnoreCase(fileContentType))
                .findFirst()
                .orElse(FileType.UNKNOWN);
    }

    //확장자만 체크
    private static FileType findByExtension(String fileExtension) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.getExtension().equalsIgnoreCase(fileExtension))
                .findFirst()
                .orElse(FileType.UNKNOWN);
    }

    //File 객체에 ContentType을 비교적 정확하게 가져오는 메서드
    private static String getContentTypeWithTika(File file) {
        Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
