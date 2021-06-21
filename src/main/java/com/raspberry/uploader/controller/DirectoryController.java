package com.raspberry.uploader.controller;

import com.raspberry.uploader.service.DirectoryService;
import com.raspberry.uploader.service.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("list")
    public List<String> listDirectoriesPerMediaType(@RequestParam("type") MediaType type) {
        final String baseDir = directoryService.determineBaseDirectory(type);
        return Arrays.stream(directoryService.listDirectories(baseDir))
                .map(File::getName)
                .collect(Collectors.toList());
    }

}
