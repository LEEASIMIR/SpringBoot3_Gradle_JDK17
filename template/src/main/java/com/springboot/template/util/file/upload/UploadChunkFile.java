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
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Getter
public class UploadChunkFile extends FileHelper {

    private final String UPLOAD_BASE_DIR;
    private final String chunkFileDir;
    private final String chunkFileName;

    public String save(String uploadFilePath, int totalChunkCnt) throws MessageException {
        log.debug("save {}", uploadFilePath);
        String resultPath = "";
        File file = super.getFile(UPLOAD_BASE_DIR + uploadFilePath);

        int index = 0;
        try (FileOutputStream finalFos = new FileOutputStream(file)) {
            for (int i = 0; i < totalChunkCnt; i++) {
                index = i;
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

//                //방법3 1857ms
//                Path chunkPath = Paths.get(UPLOAD_BASE_DIR, chunkFileDir, chunkFileName + i);
//                // Files.copy를 사용하여 chunk 파일을 최종 파일에 복사
//                Files.copy(chunkPath, finalFos);
//                // 복사 후 원본 파일 삭제
//                Files.delete(chunkPath);
            }
            resultPath = UPLOAD_BASE_DIR + uploadFilePath;
        } catch (FileNotFoundException e) {
            throw new MessageException("exception.download.not.found", new  String[]{chunkFileDir + "/" + chunkFileName+index});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultPath;
    }

    public long getFileCnt(String dirPath) {
        Path startPath = Paths.get(dirPath);
        long fileCount = 0L;
        try (Stream<Path> stream = Files.walk(startPath)) {
            fileCount = stream.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCount;
    }

    public void deleteFolder() {
        this.deleteFolder(new File(UPLOAD_BASE_DIR+chunkFileDir));
    }

    private void deleteFolder(File folder) {

        // 폴더 내 모든 파일/폴더를 배열로 가져옴
        File[] files = folder.listFiles();

        if (files != null) {
            // 배열을 순회하며 삭제
            for (File file : files) {
                // 파일이면 삭제
                if (file.isFile()) {
                    file.delete();
                } else {
                    // 폴더면 재귀 호출
                    deleteFolder(file);
                }
            }
        }
        // 폴더 내부가 모두 비워지면 폴더 자체를 삭제
        folder.delete();
    }
}
