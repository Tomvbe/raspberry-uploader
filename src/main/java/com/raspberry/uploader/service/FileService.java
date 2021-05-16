package com.raspberry.uploader.service;

import com.raspberry.uploader.exception.FileStorageException;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.cleanPath;

@Service
public class FileService {

    @Value("${app.upload.dir}")
    public String BASE_UPLOAD_DIR;

    public void uploadFiles(MultipartFile[] files, String relativeDir) {

        final String absoluteDir = BASE_UPLOAD_DIR + File.separator + relativeDir;
        final Path dirPath = Paths.get(absoluteDir);
        if (Files.notExists(dirPath)) {
            createDirectory(dirPath);
        }

        Arrays.asList(files).forEach(file -> uploadFile(file, absoluteDir));
    }

    private void createDirectory(Path dirPath) {
        try {
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            throw new FileStorageException("Could not create a new directory for the files", e);
        }
    }

    private void uploadFile(MultipartFile file, String absoluteDir) {

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("No file(s) selected");
        }

        try {
            String absoluteFileName = absoluteDir + File.separator + cleanPath(requireNonNull(file.getOriginalFilename()));
            final Path path = Paths.get(absoluteFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename(), e);
        }

    }
}
