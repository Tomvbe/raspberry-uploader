package com.raspberry.uploader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadController {

    @GetMapping("/")
    public String index() {
        return "redirect:upload";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

}
