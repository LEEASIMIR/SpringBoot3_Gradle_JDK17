package com.springboot.template.util.file;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValid {

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
     * @param fileSizeStr "5KB" , "5MB" , "5GB"
     * @return 바이트 단위의 long 값
     * @throws IllegalArgumentException 유효하지 않은 형식일 경우 발생
     */
    public static boolean isAllowSize(long targetFileSize, String fileSizeStr) {
        return targetFileSize > getFileSizeByte(fileSizeStr);
    }

    /**
     * 문자열 데이터 크기를 바이트(long)로 변환합니다.
     * 예: "5MB" -> 5242880L
     * @param fileSizeStr "5KB" , "5MB" , "5GB"
     * @return 바이트 단위의 long 값
     * @throws IllegalArgumentException 유효하지 않은 형식일 경우 발생
     */
    public static long getFileSizeByte(String fileSizeStr) {

        Pattern pattern = Pattern.compile("(\\d+)([KMGT]B)");

        //문자열 (예: "10KB", "5MB", "1GB")
        Matcher matcher = pattern.matcher(fileSizeStr.trim().toUpperCase());
        if (!matcher.matches()) {
            return getFileSizeByte("5MB");
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
