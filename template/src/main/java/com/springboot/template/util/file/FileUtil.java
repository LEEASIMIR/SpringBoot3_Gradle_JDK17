package com.springboot.template.util.file;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.util.file.upload.FileHelper;
import com.springboot.template.util.file.upload.FileInfoVO;
import com.springboot.template.util.file.upload.UploadChunkFile;
import com.springboot.template.util.file.upload.UploadMultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil extends FileHelper {

    @Value("${upload.file.dir}")
    private String UPLOAD_BASE_DIR;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    public void download(HttpServletResponse response, FileInfoVO fileInfoVO) throws MessageException {

        String physicsFilePath = fileInfoVO.physicsFilePath() + "/" + fileInfoVO.physicsFileName();
        File file = new File(UPLOAD_BASE_DIR + "/" + physicsFilePath);

        if(!file.exists()) {
            log.error("NOT FOUND :{}", file.getPath());
            throw new MessageException("exception.download.not.found", new String[]{fileInfoVO.fileName()});
        }

        String encodedFileName = URLEncoder.encode(fileInfoVO.fileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 응답 헤더 설정
        response.setContentType(fileInfoVO.fileType().getContentType());
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setHeader("Content-Length", String.valueOf(file.length()));

        // 파일 스트림을 응답 스트림으로 복사
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * File Uplaod
     * @param allowFileType {@link FileType}
     * @param maxFileSize 10MB , 10GB , 10KB 이런식으로..
     * @param uploadPath /data
     * @param file {@link MultipartFile}
     * @return {@link FileInfoVO}
     * @author 이봉용
     * @date 25. 9. 13.
     */
    public FileInfoVO uploadFile(String maxFileSize, String uploadPath, MultipartFile file, FileType... allowFileType) throws Exception {

        UploadMultipartFile upload = new UploadMultipartFile(UPLOAD_BASE_DIR, uploadPath, file);

        //경로 취약점 체크
        if(!FileValid.isAllowPath(uploadPath)) {
            String[] args = new String[]{uploadPath};
            throw new MessageException("exception.upload.not.allow.path", args);
        }

        //파일 타입 체크
        if(!FileValid.isAllowContentType(upload.getFileType(), allowFileType)) {
            String[] args = new String[]{upload.getExtension() + "," +upload.getContentType()};
            throw new MessageException("exception.upload.not.allow.type", args);
        }

        //파일 용량 체크
        if(!FileValid.isAllowSize(upload.getSizeByte(), MAX_FILE_SIZE)) {
            String[] args = new String[]{String.valueOf(upload.getSizeMB()), MAX_FILE_SIZE};
            throw new MessageException("exception.upload.not.allow.size", args);
        }
        //저장
        String savedFilePath = upload.save();

        return upload.getFileInfoVO(upload.getFileName(), savedFilePath);
    }

    /**
     * 청크 업로드
     * @author 이봉용
     * @date 25. 9. 14.
     */
    public FileInfoVO chunkUpload(MultipartFile chunk, String uploadPath, String originFileName, int currentIndex, int lastIndex) throws IOException, MessageException {

        FileInfoVO resultVO = null;

        String tempPath = "/chunkTemp";
        String chunkFileName = "chunk";

        UploadMultipartFile tempUpload = new UploadMultipartFile(UPLOAD_BASE_DIR, tempPath, chunk);
        UploadChunkFile chunkUpload = new UploadChunkFile(UPLOAD_BASE_DIR, tempPath, chunkFileName);

        //처음이라면 Chunk 파일 모두 삭제
        if(currentIndex == 0) chunkUpload.deleteFolder();

        // 청크 저장
        String savedChunkFilePath = tempUpload.save(chunkFileName + currentIndex);
        log.debug("저장한 청크파일 : {} size : {}", savedChunkFilePath, tempUpload.getSizeByte());

        resultVO = tempUpload.getFileInfoVO(chunkFileName + currentIndex, savedChunkFilePath);

        // 모든 청크가 도착했는지 확인
        long fileCnt = chunkUpload.getFileCnt(UPLOAD_BASE_DIR + tempPath);
        log.info("lastIndex {} temp file cnt {}", lastIndex, fileCnt);
        if (fileCnt == lastIndex) {
            // 모든 청크를 합쳐서 최종 파일 생성
            String savedFilePath = chunkUpload.save(uploadPath + "/" + chunkUpload.getUUIDFileName(originFileName), lastIndex);
            resultVO = tempUpload.getFileInfoVO(originFileName, savedFilePath);
            chunkUpload.deleteFolder();
        }

        return resultVO;
    }
}
