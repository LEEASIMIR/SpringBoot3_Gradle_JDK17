package com.springboot.template.util.file.upload;

import com.springboot.template.util.file.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileHelper {

    public File getFile(String uploadPath, String fileName) {
        return this.getFile(uploadPath + "/" + fileName);
    }

    public File getFile(String savedFilePath) {
        return new File(savedFilePath);
    }

    public FileInfoVO getFileInfoVO(String UPLOAD_BASE_DIR, String originFileName, String savedFilePath) {

        File file = this.getFile(savedFilePath);

        FileType fileType = FileType.findByExtensionAndMimeType(this.getExtension(file.getName()), this.getContentType(file));

        String physicsFilePath = savedFilePath.replace(UPLOAD_BASE_DIR, "").replace("/"+file.getName(), "");

        return new FileInfoVO(originFileName, physicsFilePath, file.getName(), fileType, file.length());
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
        return this.getSizeMB(sizeByte) / 1024;
    }

    public float getSizeMB(long sizeByte) {
        return this.getSizeKB(sizeByte) / 1024;
    }

    public float getSizeKB(long sizeByte) {
        return (float) sizeByte / 1024;
    }

    public String getUUIDFileName(String fileName) {
        String extension = this.getExtension(fileName);
        extension = "."+extension;
        return System.currentTimeMillis()
                + "-" + UUID.randomUUID() + "-" + fileName.replace(extension, "")
                + extension;
    }
}
