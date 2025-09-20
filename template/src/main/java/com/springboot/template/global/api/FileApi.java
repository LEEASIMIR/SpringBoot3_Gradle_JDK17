package com.springboot.template.global.api;

import com.springboot.template.common.model.ApiResponseEntity;
import com.springboot.template.util.file.base.FileHelper;
import com.springboot.template.util.file.base.FileType;
import com.springboot.template.util.file.base.FileValid;
import com.springboot.template.util.file.dto.UploadValidDto;
import com.springboot.template.util.file.vo.UploadFileInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileApi {

    @Value("${upload.file.dir}")
    private String UPLOAD_BASE_DIR;
    @Value("${upload.file.test}")
    private String UPLOAD_TEST_DIR;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String UPLOAD_MAX_SIZE;

    @PostMapping(value = "/file-upload", consumes = "multipart/form-data")
    public ApiResponseEntity<UploadFileInfoVO> fileUpload(HttpServletResponse response, @Validated @RequestPart MultipartFile file) throws Exception {

        FileHelper fileHelper = new FileHelper(UPLOAD_BASE_DIR);
        UploadFileInfoVO result = fileHelper.save(file, UPLOAD_TEST_DIR, fileHelper.getUUIDFileName(file.getName()));
        return ApiResponseEntity.ok(response, result);
    }

    @GetMapping(value = "/file-download")
    public void fileDownload(HttpServletResponse response, @RequestParam("returnFileName") String returnFileName, @RequestParam("filePath") String filePath) throws Exception {
        FileHelper fileHelper = new FileHelper(UPLOAD_BASE_DIR);
        fileHelper.download(response, "10MB", returnFileName, filePath);
    }

    @GetMapping(value = "/get-file-type")
    public ApiResponseEntity<FileType> getFileType(HttpServletResponse response, @RequestParam("fileName") String fileName, @RequestParam("contentType") String contentType) throws Exception {
        return ApiResponseEntity.ok(response, FileType.findByExtensionAndMimeType(fileName, contentType));
    }

    @PostMapping("/start-chunk-upload")
    public ApiResponseEntity<Map<String, String>> startChunkUpload(HttpServletResponse response, @RequestBody UploadValidDto dto) throws Exception {

        FileValid.valid(dto,UPLOAD_MAX_SIZE, FileType.ALL());

        Map<String, String> result = new HashMap<>();
        result.put("sessionUrl", "/chunk-upload/"+UUID.randomUUID());
        return ApiResponseEntity.ok(response, result);
    }

    @PostMapping("/chunk-upload/{sessionId}")
    public  ApiResponseEntity<UploadFileInfoVO> chunkUpload(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            @NotNull @RequestHeader("BONG-File-Range") String contentRange,
                                                            @NotNull @RequestHeader("BONG-File-Name") String fileName,
                                                            @NotNull @RequestHeader("BONG-File-Type") String fileType,
                                                            @PathVariable String sessionId,
                                                            InputStream inputStream) throws IOException {

        FileHelper fileHelper = new FileHelper(UPLOAD_BASE_DIR);

        String[] range = contentRange.split("/");

        boolean isSuccess = fileHelper.saveChunk(inputStream, sessionId, Long.parseLong(range[0]), Long.parseLong(range[1]));

        if (isSuccess) {
            UploadFileInfoVO result = fileHelper.completeChunk(fileName, UPLOAD_TEST_DIR+"/"+fileHelper.getUUIDFileName(fileName));
            return ApiResponseEntity.ok(response, result);
        } else {
            response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
            return ApiResponseEntity.other(response, HttpStatus.PERMANENT_REDIRECT, "또 줘", null);
        }
    }

}
