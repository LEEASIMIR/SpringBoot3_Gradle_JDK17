package com.springboot.template.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping("/chunk-file-upload")
    public String chunkFileUpload() {
        return "/file/chunk-file-upload";
    }
}
