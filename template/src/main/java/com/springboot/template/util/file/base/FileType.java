package com.springboot.template.util.file.base;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 파일 타입 종류 정의
 * @author 이봉용
 * @date 25. 9. 13.
 */
@Slf4j
@Getter
public enum FileType {

    UNKNOWN("unknown", "unknown", Collections.emptyList()),

    JPG("jpg", "image/jpeg", Collections.emptyList()),
    JPEG("jpeg", "image/jpeg", Collections.emptyList()),
    PNG("png", "image/png", Collections.emptyList()),
    GIF("gif", "image/gif", Collections.emptyList()),

    MP4("mp4", "video/mp4", Collections.emptyList()),
    AVI("avi", "video/x-msvideo", Collections.emptyList()),
    MOV("mov", "video/quicktime", Collections.emptyList()),
    WMV("wmv", "video/x-ms-wmv", Collections.emptyList()),
    MKV("mkv", "video/x-matroska", Collections.emptyList()),
    WEBM("webm", "video/webm", Collections.emptyList()),

    PDF("pdf", "application/pdf", Collections.emptyList()),
    DOC("doc", "application/msword", Collections.emptyList()),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", Collections.emptyList()),
    XLS("xls", "application/vnd.ms-excel", Collections.emptyList()),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Collections.emptyList()),
    PPT("ppt", "application/vnd.ms-powerpoint", Collections.emptyList()),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", Collections.emptyList()),
    TXT("txt", "text/plain", Collections.emptyList()),
    CSV("csv", "text/csv", Collections.emptyList()),

    JSON("json", "application/json", Collections.emptyList()),
    XML("xml", "application/xml", Collections.emptyList()),
    EXE("exe", "application/x-msdownload", Collections.emptyList()),
    DOSEXEC("exe", "application/x-dosexec", Collections.emptyList()),

    IMAGE("IMAGE", "file/image", List.of(JPG, JPEG, PNG, GIF)),
    DOCUMENT("DOCUMENT", "file/document", List.of(PDF, DOC, DOCX, XLS,  XLSX, PPT, PPTX,  TXT, CSV)),
    DATA("DATA", "DATA", List.of(JSON, XML)),
    VIDEO("VIDEO", "file/video", List.of(MP4, AVI, MOV, WMV, MKV, WEBM));

    private final String extension;
    private final String contentType;
    private final List<FileType> subTypes;

    FileType(String extension, String contentType, List<FileType> subTypes) {
        this.extension = extension;
        this.contentType = contentType;
        this.subTypes = subTypes;
    }

    public static FileType[] ALL() {
        return FileType.values();
    }

    public static FileType findByExtensionAndMimeType(@Nonnull MultipartFile file) {
        return findByExtensionAndMimeType(getExtension(Objects.requireNonNull(file.getOriginalFilename())), Objects.requireNonNull(file.getContentType()));
    }

    public static String getExtension(@Nonnull String fileName) {
        String extension = "";
        if (fileName.lastIndexOf(".") != -1) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }

    public static String getContentType(File file) {
        Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    public static FileType findByFileNameAndMimeType(@Nonnull String fileName, @Nonnull String fileContentType) {
        return findByExtensionAndMimeType(getExtension(fileName), fileContentType);
    }

    //하드 체크, 둘다 만족
    public static FileType findByExtensionAndMimeType(@Nonnull String fileExtension, @Nonnull String fileContentType) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.getExtension().equalsIgnoreCase(fileExtension) &&
                        type.getContentType().equalsIgnoreCase(fileContentType.split(";")[0]))
                .findFirst()
                .orElse(FileType.UNKNOWN);
    }

    //확장자만 체크
    public static FileType findByExtension(@Nonnull String fileExtension) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.getExtension().equalsIgnoreCase(fileExtension))
                .findFirst()
                .orElse(FileType.UNKNOWN);
    }
}
