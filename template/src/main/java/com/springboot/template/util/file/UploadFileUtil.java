package com.springboot.template.util.file;

import com.springboot.template.common.exception.MessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class UploadFileUtil {

    @Value("${upload.file.dir}")
    private String uploadBaseDir;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String serverMaxFileSize;

    private List<FileType> allowFileTypes = null;
    private long allowMaxFileSizeByte = 0L;

    public void init(FileType[] allowFileTypes) throws MessageException {
        this.allowFileTypes = List.of(allowFileTypes);
        this.allowMaxFileSizeByte = this.getServerMaxFileSizeByte();
    }

    public void init(FileType[] allowFileTypes, long allowMaxFileSizeByte) {
        this.allowFileTypes = List.of(allowFileTypes);
        this.allowMaxFileSizeByte = allowMaxFileSizeByte;
    }

    /**
     * File Uplaod
     * @param dirPath 업로드할 경로
     * @param file 파일
     * @param hardCheck 확장자, 컨텐츠타입 모두 체크 할지 여부
     * @author 이봉용
     * @date 25. 9. 13.
     */
    public FileInfoVO uploadFile(String dirPath, MultipartFile file, boolean hardCheck) throws Exception {

        if(this.allowFileTypes == null || this.allowMaxFileSizeByte < 1L) {
            throw new MessageException("RUN init() FIRST");
        }

        String physicsFilePath = this.uploadBaseDir + dirPath;
        FileType fileType = FileType.getFileTypeByMultipartFile(file, hardCheck);
        log.info("{} {} {}", LocaleContextHolder.getLocale(), FileType.getExtension(file),  FileType.getContentType(file));

        if(!isValidPath(dirPath)) {
            String[] args = new String[]{dirPath};
            throw new MessageException("exception.upload.not.allow.path", args);
        }
        if(!this.allowFileTypes.contains(fileType)) {
            String[] args = new String[]{fileType.getExtension() + "," +fileType.getContentType()};
            throw new MessageException("exception.upload.not.allow.type", args);
        }
        if(file.getSize() > this.allowMaxFileSizeByte) {
            String[] args = new String[]{String.valueOf(file.getSize())};
            throw new MessageException("exception.upload.not.allow.size", args);
        }

        File dir = new File(physicsFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String physicsFileName = System.currentTimeMillis()
                + "-" + UUID.randomUUID()
                + "." + fileType.getExtension();

        File dest = new File(physicsFilePath + "/" + physicsFileName);
        file.transferTo(dest);

        return new FileInfoVO(
                file.getOriginalFilename(),
                dirPath,
                physicsFileName,
                fileType,
                file.getSize()
        );
    }

    /**
     * 허용되지 않는 문자가 발견되면 false를 반환
     * @param path 업로드 할 경로
     * @author 이봉용
     * @date 25. 9. 13.
     */
    private boolean isValidPath(String path) {
        String regex = "[^a-zA-Z/_-]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        return !matcher.find();
    }

    /**
     * 문자열 데이터 크기를 바이트(long)로 변환합니다.
     * 예: "5MB" -> 5242880L
     * @return 바이트 단위의 long 값
     * @throws IllegalArgumentException 유효하지 않은 형식일 경우 발생
     */
    private long getServerMaxFileSizeByte() {

        Pattern pattern = Pattern.compile("(\\d+)([KMGT]B)");

        //문자열 (예: "10KB", "5MB", "1GB")
        Matcher matcher = pattern.matcher(this.serverMaxFileSize.trim().toUpperCase());
        if (!matcher.matches()) {
            this.serverMaxFileSize = "5MB";
            return this.getServerMaxFileSizeByte();
        }

        long value = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2);

        return switch (unit) {
            case "KB" -> value * 1024;
            case "MB" -> value * 1024 * 1024;
            case "GB" -> value * 1024 * 1024 * 1024;
            case "TB" -> value * 1024L * 1024 * 1024 * 1024;
            default -> 5 * 1024 * 1024;
        };
    }
}
