package com.springboot.template.util.file;


public record FileInfoVO(String fileName, String physicsFilePath, String physicsFileName, FileType fileType,
                         long sizeByte) {
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
