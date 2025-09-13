package com.springboot.template.test.api;

import com.springboot.template.common.model.ApiResponseEntity;
import com.springboot.template.test.service.TestServiceImpl;
import com.springboot.template.util.external.tcp.client.ClientGateway;
import com.springboot.template.util.file.DownloadFileUtil;
import com.springboot.template.util.file.FileInfoVO;
import com.springboot.template.util.file.FileType;
import com.springboot.template.util.file.UploadFileUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestApi {

    private final TestServiceImpl testServiceImpl;
    private final ClientGateway clientGateway;
    private final UploadFileUtil uploadFileUtil;
    private final DownloadFileUtil downloadFileUtil;

    @GetMapping("/test/tcp-send")
    public String tcpSend(@RequestParam String message) {
        log.info("{}", clientGateway.send(message));
        return "";
    }

    @GetMapping("/test/throwsError")
    public String throwsError() {
        return testServiceImpl.throwsError();
    }

    @PostMapping(value = "/file-upload", consumes = "multipart/form-data")
    public ApiResponseEntity<FileInfoVO> fileUpload(@Validated @RequestPart MultipartFile file) throws Exception {
        this.uploadFileUtil.init(new FileType[]{FileType.JPG, FileType.PNG, FileType.GIF, FileType.JPEG});
        FileInfoVO result = uploadFileUtil.uploadFile("/data", file, true);
        return ApiResponseEntity.ok(result);
    }

    @GetMapping(value = "/file-download")
    public void fileDownload(HttpServletResponse response) throws Exception {
        FileInfoVO fileInfoVO = new FileInfoVO("스크린샷 2025-06-23 032712.png", "/data", "1757726049040-032a0217-9463-41b7-aba5-c394e0714f28.png", FileType.PNG, 13056);
        downloadFileUtil.download(response, fileInfoVO);
    }
}
