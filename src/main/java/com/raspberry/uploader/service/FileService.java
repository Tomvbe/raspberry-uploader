package com.raspberry.uploader.service;

import com.raspberry.uploader.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.cleanPath;

@Service
public class FileService {

    public void uploadFiles(MultipartFile[] files, String dir) {
        Arrays.asList(files).forEach(file -> uploadFile(file, dir));
    }

    private void uploadFile(MultipartFile file, String dir) {

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("No file(s) selected");
        }

        try {
            String absoluteFileName = dir + File.separator + cleanPath(requireNonNull(file.getOriginalFilename()));
            final Path path = Paths.get(absoluteFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename(), e);
        }

    }
}
