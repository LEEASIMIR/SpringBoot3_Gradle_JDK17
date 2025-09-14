package com.springboot.template.global.api;

import com.springboot.template.common.exception.MessageException;
import com.springboot.template.common.model.ApiResponseEntity;
import com.springboot.template.util.file.*;
import com.springboot.template.util.file.upload.FileHelper;
import com.springboot.template.util.file.upload.FileInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileApi {

    private final FileUtil fileUtil;

    @PostMapping(value = "/file-upload", consumes = "multipart/form-data")
    public ApiResponseEntity<FileInfoVO> fileUpload(@Validated @RequestPart MultipartFile file) throws Exception {
        FileInfoVO result = this.fileUtil.uploadFile("10MB", "/data", file, FileType.EXE);
        return ApiResponseEntity.ok(result);
    }

    @GetMapping(value = "/file-download")
    public void fileDownload(HttpServletResponse response, @RequestBody FileDownloadReqDto fileDownloadDto) throws Exception {
//        FileHelper fileHelper = new FileHelper();
//        FileInfoVO fileInfoVO = fileHelper.getFileInfoVO(fileDownloadDto.getReturnFileName(), fileDownloadDto.getFilePath());
//        this.fileUtil.download(response, fileInfoVO);
    }

    @GetMapping(value = "/get-file-type")
    public ApiResponseEntity<FileTypeResDto> getFileType(@RequestParam("fileName") String fileName, @RequestParam("contentType") String contentType) throws Exception {
        FileTypeResDto dto = new FileTypeResDto();
        dto.setFileType(FileType.findByExtensionAndMimeType(fileName, contentType));

        return ApiResponseEntity.ok(dto);
    }

    @PostMapping("/chunk-upload")
    public ApiResponseEntity<FileInfoVO> handleChunkUpload(
            @RequestParam("file") MultipartFile chunk,
            @RequestParam("fileName") String fileName,
            @RequestParam("currentIndex") int currentIndex,
            @RequestParam("lastIndex") int lastIndex) throws IOException, MessageException {
        FileInfoVO result = fileUtil.chunkUpload(chunk, "/data", fileName, currentIndex, lastIndex);

        return ApiResponseEntity.ok(result);
    }
}
