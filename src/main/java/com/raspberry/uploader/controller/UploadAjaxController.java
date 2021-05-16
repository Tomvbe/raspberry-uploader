package com.raspberry.uploader.controller;

import com.raspberry.uploader.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("ajax")
public class UploadAjaxController {

    private final FileService fileService;

    public UploadAjaxController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public void multiFileUpload(@RequestParam("files") MultipartFile[] files,
                                           @RequestParam("directory") String dir) {
        fileService.uploadFiles(files, dir);
    }

}
