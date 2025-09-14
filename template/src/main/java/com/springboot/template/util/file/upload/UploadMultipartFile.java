package com.springboot.template.util.file.upload;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.util.file.FileType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter
public class UploadMultipartFile extends FileHelper {

    private final String UPLOAD_BASE_DIR;
    private final MultipartFile file;
    private final String fileName;
    private final String physicsFilePath;
    private final String uuidFileName;
    private final FileType fileType;
    private final long sizeByte;

    public UploadMultipartFile(String UPLOAD_BASE_DIR, String physicsFilePath, MultipartFile file) {
        this.UPLOAD_BASE_DIR = UPLOAD_BASE_DIR;
        this.file = file;
        this.fileName = this.file.getName();
        this.sizeByte = file.getSize();
        this.fileType = FileType.findByExtensionAndMimeType(super.getExtension(this.fileName), super.getContentType(this.file));
        this.physicsFilePath = this.UPLOAD_BASE_DIR+physicsFilePath;
        this.uuidFileName = super.getUUIDFileName(this.fileName);
    }

    public String save(String fileName) throws IOException, MessageException {
        this.mkdir(this.physicsFilePath);
        this.file.transferTo(this.getFile(this.physicsFilePath, fileName));
        return this.physicsFilePath + "/" + fileName;
    }

    public String save() throws IOException, MessageException {
        this.mkdir(this.physicsFilePath);
        this.file.transferTo(this.getFile(this.physicsFilePath, this.uuidFileName));
        return this.physicsFilePath + "/" + this.uuidFileName;
    }

    public FileInfoVO getFileInfoVO(String originFileName, String savedFilePath) throws MessageException {
        return super.getFileInfoVO(UPLOAD_BASE_DIR, originFileName, savedFilePath);
    }

    public String getExtension() {
        return super.getExtension(this.fileName);
    }

    public String getContentType() {
        return super.getContentType(this.file);
    }

    private boolean mkdir(String dirPath) {
        boolean result = false;

        File dir = new File(dirPath);

        if (!dir.exists()) result = dir.mkdirs();

        return result;
    }

    public float getSizeGB() {
        return this.getSizeMB() / 1024;
    }

    public float getSizeMB() {
        return this.getSizeKB() / 1024;
    }

    public float getSizeKB() {
        return (float) this.sizeByte / 1024;
    }

}
