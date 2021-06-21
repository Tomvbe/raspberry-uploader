package com.raspberry.uploader.controller;

import com.raspberry.uploader.service.DirectoryService;
import com.raspberry.uploader.service.FileService;
import com.raspberry.uploader.service.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("ajax")
public class UploadAjaxController {

    private final FileService fileService;
    private final DirectoryService directoryService;

    public UploadAjaxController(FileService fileService, DirectoryService directoryService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public void multiFileUpload(@RequestParam("files") MultipartFile[] files,
                                @RequestParam("directory") String dir,
                                @RequestParam("type") MediaType type) {
        final String baseDir = directoryService.determineBaseDirectory(type, dir);
        directoryService.initializeDirectory(baseDir);
        fileService.uploadFiles(files, baseDir);
    }

}
