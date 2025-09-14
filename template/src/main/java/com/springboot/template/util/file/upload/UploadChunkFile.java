package com.springboot.template.util.file.upload;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.util.file.FileType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Getter
public class UploadChunkFile extends FileHelper {

    private final String UPLOAD_BASE_DIR;
    private final String chunkFileDir;
    private final String chunkFileName;

    public String save(String uploadFilePath, int lastIndex) throws MessageException {
        log.debug("save {}", uploadFilePath);
        String resultPath = "";
        File file = super.getFile(UPLOAD_BASE_DIR + uploadFilePath);

        try (FileOutputStream finalFos = new FileOutputStream(file)) {
            for (int i = 0; i <= lastIndex; i++) {
//                File chunkFile = new File(UPLOAD_BASE_DIR+chunkFileDir, chunkFileName+i);
//
//                //방법1 1988ms
//                byte[] chunkBytes = Files.readAllBytes(chunkFile.toPath());
//                finalFos.write(chunkBytes);
//                chunkFile.delete();
//
                //방법2 1860ms
                File chunkFile = new File(UPLOAD_BASE_DIR+chunkFileDir, chunkFileName+i);
                try (BufferedInputStream chunkBis = new BufferedInputStream(new FileInputStream(chunkFile))) {
                    byte[] buffer = new byte[8192]; // 8KB 버퍼
                    int bytesRead;
                    while ((bytesRead = chunkBis.read(buffer)) != -1) {
                        finalFos.write(buffer, 0, bytesRead);
                    }
                }
                chunkFile.delete();

//                //방법3 1857ms
//                Path chunkPath = Paths.get(UPLOAD_BASE_DIR, chunkFileDir, chunkFileName + i);
//                // Files.copy를 사용하여 chunk 파일을 최종 파일에 복사
//                Files.copy(chunkPath, finalFos);
//                // 복사 후 원본 파일 삭제
//                Files.delete(chunkPath);
            }
            Files.deleteIfExists(Path.of(UPLOAD_BASE_DIR+chunkFileDir));
            resultPath = UPLOAD_BASE_DIR + uploadFilePath;
        } catch (FileNotFoundException e) {
            throw new MessageException("exception.download.not.found", new  String[]{chunkFileDir + "/" + chunkFileName});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultPath;
    }
}
