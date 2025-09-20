package com.springboot.template.global.api;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.common.model.ApiResponseEntity;
import com.springboot.template.util.file.*;
import com.springboot.template.util.file.base.FileType;
import com.springboot.template.util.file.dto.UploadChunkDto;
import com.springboot.template.util.file.vo.UploadFileInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileApi {

    private final FileUtil fileUtil;

    @PostMapping(value = "/file-upload", consumes = "multipart/form-data")
    public ApiResponseEntity<UploadFileInfoVO> fileUpload(@Validated @RequestPart MultipartFile file) throws Exception {
        UploadFileInfoVO result = this.fileUtil.uploadFile("10MB", "/data", file, FileType.EXE);
        return ApiResponseEntity.ok(result);
    }

    @GetMapping(value = "/file-download")
    public void fileDownload(HttpServletResponse response, @RequestParam("returnFileName") String returnFileName, @RequestParam("filePath") String filePath) throws Exception {
        this.fileUtil.download(response, returnFileName, filePath);
    }

    @GetMapping(value = "/get-file-type")
    public ApiResponseEntity<FileType> getFileType(@RequestParam("fileName") String fileName, @RequestParam("contentType") String contentType) throws Exception {
        return ApiResponseEntity.ok(FileType.findByExtensionAndMimeType(fileName, contentType));
    }

    @PostMapping("/chunk-upload")
    public ApiResponseEntity<UploadFileInfoVO> handleChunkUpload(@ModelAttribute UploadChunkDto dto) throws IOException, MessageException {
        UploadFileInfoVO result = fileUtil.chunkUpload(dto, "/data");

        return ApiResponseEntity.ok(result);
    }
}
