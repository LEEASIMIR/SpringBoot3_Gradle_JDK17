package com.springboot.template.util.file.base;

import com.springboot.template.util.file.exception.InvalidUploadChunkException;
import com.springboot.template.util.file.vo.UploadFileInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileHelper {

    private final String UPLOAD_BASE_DIR;
    private Path temp;

    public FileHelper(String UPLOAD_BASE_DIR) throws IOException {
        this.UPLOAD_BASE_DIR = UPLOAD_BASE_DIR;
        Path basePath = Paths.get(UPLOAD_BASE_DIR);
        if(!Files.exists(basePath)) {
            Files.createDirectories(basePath);
        }
    }

    public File getFile(String dir, String fileName) {
        return getFile(dir + "/" + fileName);
    }

    public File getFile(String savedFilePath) {
        return Paths.get(savedFilePath).toFile();
    }

    public UploadFileInfoVO getFileInfoVO(String originFileName, String savedFilePath) {

        Path basePath = Paths.get(UPLOAD_BASE_DIR);
        String baseLocation = basePath.toAbsolutePath().toString();
        Path savedPath = Paths.get(savedFilePath);
        String savedLocation = savedPath.getParent().toAbsolutePath().toString();

        File file = savedPath.toFile();

        FileType fileType = FileType.findByExtensionAndMimeType(FileType.getExtension(file.getName()), FileType.getContentType(file));

        String physicsFilePath = savedLocation.replace(baseLocation, "");

        return new UploadFileInfoVO(originFileName, physicsFilePath, file.getName(), fileType, file.length());
    }

    public String getUUIDFileName(String fileName) {
        String extension = FileType.getExtension(fileName);
        extension = "."+extension;
        return System.currentTimeMillis()
                + "-" + UUID.randomUUID() + "-" + fileName.replace(extension, "")
                + extension;
    }

    /**
     * 속도 제한 다운로드
     * @param maxMs 최대 ?mb/s 로 내려줄지 예: 10MB, 5MB, 5KB 등
     * @param returnFileName 돌려줄때 파일명
     * @param filePath UPLOAD_BASE_DIR를 제외한 경로
     * @author 이봉용
     * @date 25. 9. 21.
     */
    public void download(HttpServletResponse response, String maxMs, String returnFileName, String filePath) throws FileNotFoundException {

        Path path = Paths.get(UPLOAD_BASE_DIR+filePath);
        File file = path.toFile();

        UploadFileInfoVO fileInfoVO = this.getFileInfoVO(returnFileName, file.getPath());

        if(!file.exists()) {
            log.error("NOT FOUND :{}", file.getPath());
            throw new FileNotFoundException(file.getPath());
        }

        String encodedFileName = URLEncoder.encode(fileInfoVO.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 응답 헤더 설정
        response.setContentType(fileInfoVO.getFileType().getContentType());
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setHeader("Content-Length", String.valueOf(file.length()));

        // 파일 스트림을 응답 스트림으로 복사
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            long bytesPerSecond = getFileSizeByte(maxMs); // 10MB/s 제한
            long start_time = System.nanoTime();
            long bytesReadSinceStart = 0;

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                bytesReadSinceStart += bytesRead;

                // 속도 제한 로직
                long elapsed_time = System.nanoTime() - start_time;
                long expected_time = (bytesReadSinceStart * 1_000_000_000) / bytesPerSecond;

                if (elapsed_time < expected_time) {
                    long sleepTime = (expected_time - elapsed_time) / 1_000_000;
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 일반 업로드
     * @author 이봉용
     * @date 25. 9. 21.
     */
    public UploadFileInfoVO save(MultipartFile file, String dirPath, String fileName) throws IOException {

        Path path = Paths.get(UPLOAD_BASE_DIR, dirPath);
        if(!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        File targetFile = this.getFile(dirPath, fileName);
        file.transferTo(targetFile);

        return this.getFileInfoVO(fileName, targetFile.getPath());
    }

    /**
     * 청크 스트림 업로드<br>현재 전체 청크 업로드 구현완, 이어쓰기 가능(예시 프론트 미구현)<br>
     * 완료 후 completeChunk 를 실행 해야 함
     * @author 이봉용
     * @date 25. 9. 21.
     */
    public boolean saveChunk(InputStream inputStream, String tempFileName, long start, long total) throws IOException {
        this.temp = Paths.get(UPLOAD_BASE_DIR).resolve("temp").resolve(tempFileName);

        if(!Files.exists(temp.getParent())) {
            Files.createDirectories(temp.getParent());
        }

        log.info("temp: {}, tempFileName: {}, start: {}, total: {}", temp.toAbsolutePath(), tempFileName, start, total);

        if (Files.exists(temp)) {
            long currentSize = Files.size(temp);
            if (start != currentSize) {
                throw new InvalidUploadChunkException("Invalid upload chunk: Mismatched offset.");
            }
        }

        this.writeStream(inputStream, temp);

        return start == total;

    }

    public void saveChunkStream(InputStream inputStream, String tempFileName, long total) throws IOException {
        this.temp = Paths.get(UPLOAD_BASE_DIR).resolve("temp").resolve(tempFileName);

        if(!Files.exists(temp.getParent())) {
            Files.createDirectories(temp.getParent());
        }

        log.info("temp: {}, tempFileName: {}, total: {}", temp.toAbsolutePath(), tempFileName, total);

        this.writeStream(inputStream, temp);

        Files.size(temp);

    }

    private void writeStream(InputStream inputStream, Path path) {
        try (var outputStream = Files.newOutputStream(temp, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 청크 업로드 완료 후, 지정 위치로 이동 및 VO 리턴
     * @author 이봉용
     * @date 25. 9. 21.
     */
    public UploadFileInfoVO completeChunk(String originFileName, String filePath) throws IOException {

        if(filePath.startsWith("/")) filePath = filePath.substring(1);

        Path targetPath = Paths.get(this.UPLOAD_BASE_DIR, filePath);

        Path fileFullPath = this.moveFile(this.temp, targetPath);

        Files.deleteIfExists(this.temp);

        return this.getFileInfoVO(originFileName, fileFullPath.toAbsolutePath().toString());
    }

    private Path moveFile(Path currentPath, Path targetPath) throws IOException {
        Path targetDir = targetPath.getParent();
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        log.info("currentPath: {},  targetPath: {}", currentPath, targetPath);
        return Files.move(currentPath, targetPath);
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

        if(!matcher.matches()) {
            return 5 * 1024 * 1024;
        } else {
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
    public static float getSizeGB(long sizeByte) {
        return FileHelper.getSizeMB(sizeByte) / 1024;
    }

    public static float getSizeMB(long sizeByte) {
        return FileHelper.getSizeKB(sizeByte) / 1024;
    }

    public static float getSizeKB(long sizeByte) {
        return (float) sizeByte / 1024;
    }


}
