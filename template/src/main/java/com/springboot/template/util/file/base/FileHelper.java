package com.springboot.template.util.file.base;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.util.file.vo.UploadFileInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class FileHelper {

    public File getFile(String dir, String fileName) {
        return getFile(dir + "/" + fileName);
    }

    public File getFile(String savedFilePath) {
        return new File(savedFilePath);
    }

    public UploadFileInfoVO getFileInfoVO(String UPLOAD_BASE_DIR, String originFileName, String savedFilePath) {

        File file = getFile(savedFilePath);

        FileType fileType = FileType.findByExtensionAndMimeType(getExtension(file.getName()), getContentType(file));

        String physicsFilePath = savedFilePath.replace(UPLOAD_BASE_DIR, "").replace("/"+file.getName(), "");

        return new UploadFileInfoVO(originFileName, physicsFilePath, file.getName(), fileType, file.length());
    }

    public String getExtension(String fileName) {
        String extension = "";
        if (fileName.lastIndexOf(".") != -1) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }

    public String getContentType(MultipartFile multipartFile) {
        return multipartFile.getContentType();
    }

    public String getContentType(File file) {
        Tika tika = new Tika();
        try {
            return tika.detect(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    public float getSizeGB(long sizeByte) {
        return getSizeMB(sizeByte) / 1024;
    }

    public float getSizeMB(long sizeByte) {
        return getSizeKB(sizeByte) / 1024;
    }

    public float getSizeKB(long sizeByte) {
        return (float) sizeByte / 1024;
    }

    public String getUUIDFileName(String fileName) {
        String extension = getExtension(fileName);
        extension = "."+extension;
        return System.currentTimeMillis()
                + "-" + UUID.randomUUID() + "-" + fileName.replace(extension, "")
                + extension;
    }

    public boolean mkdir(String dirPath) {
        boolean result = false;

        File dir = new File(dirPath);

        if (!dir.exists()) result = dir.mkdirs();

        return result;
    }

    public void deleteFolder(String dirPath) {
        deleteFolder(getFile(dirPath));
    }
    public void deleteFolder(File dir) {

        // 폴더 내 모든 파일/폴더를 배열로 가져옴
        File[] files = dir.listFiles();

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
        dir.delete();
    }

    public File save(MultipartFile file, String dirPath, String fileName) throws IOException {
        this.mkdir(dirPath);
        file.transferTo(this.getFile(dirPath, fileName));
        return this.getFile(dirPath, fileName);
    }

    public File merge(String UPLOAD_BASE_DIR, String chunkFileDir, String chunkFileName, String uploadFilePath, int totalChunkCnt) throws MessageException {
        log.debug("save {}", uploadFilePath);

        File file = this.getFile(UPLOAD_BASE_DIR + uploadFilePath);

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
                File chunkFile = new File(UPLOAD_BASE_DIR + chunkFileDir, chunkFileName + i);
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

            this.deleteFolder(new File(UPLOAD_BASE_DIR + chunkFileDir));

        } catch (FileNotFoundException e) {
            throw new MessageException("exception.download.not.found", new String[]{chunkFileDir + "/" + chunkFileName + index});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.getFile(UPLOAD_BASE_DIR + uploadFilePath);
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

}
