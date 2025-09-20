package com.springboot.template.util.file.vo;


import com.springboot.template.util.file.base.FileType;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileInfoVO {

    private final String fileName;
    private final String physicsFilePath;
    private final String physicsFileName;
    private final FileType fileType;
    private final long sizeByte;

    public UploadFileInfoVO(String originFileName, String physicsFilePath, String physicsFileName, FileType fileType, long sizeByte) {
        this.fileName = originFileName;
        this.physicsFilePath = physicsFilePath.replace("\\", "/");
        this.physicsFileName = physicsFileName;
        this.fileType = fileType;
        this.sizeByte = sizeByte;
    }

    public String getPhysicsFileName() {
        return physicsFileName;
    }

    public String getPhysicsFilePath() {
        return physicsFilePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public long getSizeByte() {
        return this.sizeByte;
    }

    private String getPath(String path) {
        Path p = Paths.get(path);
        return p.toAbsolutePath().toString();
    }
}
