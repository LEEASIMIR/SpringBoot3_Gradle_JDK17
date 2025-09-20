package com.springboot.template.util.file;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.util.file.base.FileHelper;
import com.springboot.template.util.file.base.FileType;
import com.springboot.template.util.file.base.FileValid;
import com.springboot.template.util.file.dto.UploadChunkDto;
import com.springboot.template.util.file.vo.UploadFileInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileUtil {

    @Value("${upload.file.dir}")
    private String UPLOAD_BASE_DIR;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;
    private final FileHelper fileHelper = new FileHelper();

    public void download(HttpServletResponse response, String returnFileName, String filePath) throws MessageException {

        File file = new File(UPLOAD_BASE_DIR + filePath);

        UploadFileInfoVO fileInfoVO = fileHelper.getFileInfoVO(UPLOAD_BASE_DIR, returnFileName, file.getPath());

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
     * @return {@link UploadFileInfoVO}
     * @author 이봉용
     * @date 25. 9. 13.
     */
    public UploadFileInfoVO uploadFile(String maxFileSize, String uploadPath, MultipartFile file, FileType... allowFileType) throws Exception {

        FileType fileType = FileType.findByExtensionAndMimeType(file);

        //경로 취약점 체크
        if(!FileValid.isAllowPath(uploadPath)) {
            String[] args = new String[]{uploadPath};
            throw new MessageException("exception.upload.not.allow.path", args);
        }

        //파일 타입 체크
        if(!FileValid.isAllowContentType(fileType, allowFileType)) {
            String[] args = new String[]{fileHelper.getExtension(file.getOriginalFilename()) + "," +file.getContentType()};
            throw new MessageException("exception.upload.not.allow.type", args);
        }

        //파일 용량 체크
        if(!FileValid.isAllowSize(file.getSize(), maxFileSize)) {
            String[] args = new String[]{String.valueOf(fileHelper.getSizeMB(file.getSize())), MAX_FILE_SIZE};
            throw new MessageException("exception.upload.not.allow.size", args);
        }
        //저장
        File savedFile = fileHelper.save(file, uploadPath, fileHelper.getUUIDFileName(file.getOriginalFilename()));

        return fileHelper.getFileInfoVO(UPLOAD_BASE_DIR, file.getOriginalFilename(), savedFile.getPath());
    }

    /**
     * 청크 업로드
     * @author 이봉용
     * @date 25. 9. 14.
     */
    public UploadFileInfoVO chunkUpload(UploadChunkDto dto, String uploadPath) throws IOException, MessageException {
        return this.chunkUpload(dto.getChunk(), dto.getUniqueTempDirName(), uploadPath, dto.getOriginFileName(), dto.getCurrentIndex(), dto.getTotalChunkCnt());
    }
    public UploadFileInfoVO chunkUpload(MultipartFile chunk, String tempDirName, String uploadPath, String originFileName, int currentIndex, int totalChunkCnt) throws IOException, MessageException {

        UploadFileInfoVO resultVO = null;

        String tempPath = "/chunk_"+tempDirName+"_temp";
        String chunkFileName = "chunk"+originFileName.replace(".", "_");

        //처음이라면 Chunk 파일 모두 삭제
        if(currentIndex == 0) fileHelper.deleteFolder(UPLOAD_BASE_DIR + tempPath);

        // 청크 저장
        File savedFile = fileHelper.save(chunk, UPLOAD_BASE_DIR+tempPath, chunkFileName + currentIndex);
        log.info("저장한 청크파일 : {} size : {}", savedFile.getPath(), fileHelper.getSizeMB(savedFile.length()));

        resultVO = fileHelper.getFileInfoVO(UPLOAD_BASE_DIR, chunkFileName + currentIndex, savedFile.getPath());

        // 모든 청크가 도착했는지 확인
        long fileCnt = fileHelper.getFileCnt(UPLOAD_BASE_DIR + tempPath);
        log.info("totalChunkCnt {} temp file cnt {}", totalChunkCnt, fileCnt);
        if (fileCnt == totalChunkCnt) {
            // 모든 청크를 합쳐서 최종 파일 생성
            File mergedFile = fileHelper.merge(UPLOAD_BASE_DIR, tempPath, chunkFileName, uploadPath + "/" + fileHelper.getUUIDFileName(originFileName), totalChunkCnt);
            resultVO = fileHelper.getFileInfoVO(UPLOAD_BASE_DIR, originFileName, mergedFile.getPath());
        }

        return resultVO;
    }
}
