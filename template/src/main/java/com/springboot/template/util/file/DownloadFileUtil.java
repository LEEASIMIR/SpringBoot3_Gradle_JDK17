package com.springboot.template.util.file;

import com.springboot.template.common.exception.MessageException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class DownloadFileUtil {

    @Value("${upload.file.dir}")
    private String uploadBaseDir;

    public void download(HttpServletResponse response, FileInfoVO fileInfoVO) throws MessageException, UnsupportedEncodingException {

        String physicsFilePath = fileInfoVO.physicsFilePath() + "/" + fileInfoVO.physicsFileName();
        File file = new File(uploadBaseDir + "/" + physicsFilePath);

        if(!file.exists()) {
            log.error("NOT FOUND :{}", file.getPath());
            throw new MessageException("exception.download.not.found", new String[]{fileInfoVO.fileName()});
        }

        String encodedFileName = URLEncoder.encode(fileInfoVO.fileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 응답 헤더 설정
        response.setContentType(fileInfoVO.fileType().getContentType());
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setHeader("Content-Length", String.valueOf(fileInfoVO.sizeByte()));

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
}
