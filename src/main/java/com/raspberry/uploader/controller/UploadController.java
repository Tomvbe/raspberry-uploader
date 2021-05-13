package com.raspberry.uploader.controller;

import com.raspberry.uploader.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:upload";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String multiFileUpload(@RequestParam("files") MultipartFile[] files,
                                  @RequestParam("directory") String dir,
                                  RedirectAttributes redirectAttributes) {
        final String uploadPath = fileService.uploadFiles(files, dir);
        redirectAttributes.addFlashAttribute("uploadPath", uploadPath);
        return "redirect:upload";
    }

}
