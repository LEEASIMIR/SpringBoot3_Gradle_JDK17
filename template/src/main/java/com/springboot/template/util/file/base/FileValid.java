package com.springboot.template.util.file.base;

import com.springboot.template.util.file.dto.UploadValidDto;
import com.springboot.template.util.file.exception.NotAllowPathException;
import com.springboot.template.util.file.exception.NotAllowSizeException;
import com.springboot.template.util.file.exception.NotAllowTypeException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValid {

    public static void valid(UploadValidDto dto, String maxFileSize, FileType... allowFileType) throws NotAllowPathException, NotAllowTypeException, NotAllowSizeException {

        FileType fileTypeObj = FileType.findByFileNameAndMimeType(dto.getFileName(), dto.getFileType());

        //경로 취약점 체크
        if(!FileValid.isAllowPath(dto.getUploadPath())) {
            throw new NotAllowPathException(dto.getUploadPath());
        }

        //파일 타입 체크
        if(!FileValid.isAllowContentType(fileTypeObj, allowFileType)) {
            throw new NotAllowTypeException(FileType.getExtension(dto.getFileName()) + ", " +dto.getFileType());
        }

        //파일 용량 체크
        if(!FileValid.isAllowSize(dto.getFileSize(), maxFileSize)) {
            throw new NotAllowSizeException(dto.getFileSize() + ", " + maxFileSize);
        }
    }

    /**
     * @param targetFileType 체크할 대상 {@link FileType}
     * @param allowFileType 허용할 {@link FileType}
     * @return 허용하면 {@code true}
     * @author 이봉용
     * @date 25. 9. 14.
     */
    public static boolean isAllowContentType(FileType targetFileType, FileType... allowFileType) {
        //파일 타입 체크
        return Arrays.stream(allowFileType).anyMatch(type ->
                type.getExtension().equalsIgnoreCase(targetFileType.getExtension())
                        && type.getContentType().equalsIgnoreCase(targetFileType.getContentType())
        );
    }

    /**
     * 경로 문자열에 허용되지 않는 문자가 발견되면 false를 반환
     * @param targetPath 업로드 할 경로
     * @return 허용하지 않는 문자열 발견 시 {@code false}
     * @author 이봉용
     * @date 25. 9. 13.
     */
    public static boolean isAllowPath(String targetPath) {
        String regex = "[^a-zA-Z/_-]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(targetPath);
        return !matcher.find();
    }

    /**
     * 용량 제한
     * 예: "5MB" -> 5242880L
     * @param maxFileSizeStr "5KB" , "5MB" , "5GB"
     * @return 바이트 단위의 long 값
     * @throws IllegalArgumentException 유효하지 않은 형식일 경우 발생
     */
    public static boolean isAllowSize(long targetFileSize, String maxFileSizeStr) {
        return FileHelper.getFileSizeByte(maxFileSizeStr) > targetFileSize;
    }

}
