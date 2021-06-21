package com.raspberry.uploader.service;

import com.raspberry.uploader.config.MediaTypeConfig;
import com.raspberry.uploader.exception.FileStorageException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumMap;

@Service
public class DirectoryService {

    private final EnumMap<MediaType, MediaTypeConfig.Directory> directories;

    public DirectoryService(MediaTypeConfig mediaTypeConfig) {
        this.directories = mediaTypeConfig.getDirectories();
    }

    @PostConstruct
    private void init() {
        directories.values().forEach(dir -> initializeDirectory(dir.getPath()));
    }

    public String determineBaseDirectory(MediaType type) {
        return directories.get(type).getPath();
    }

    public String determineBaseDirectory(MediaType type, String dir) {
        return determineBaseDirectory(type) + File.separator + dir;
    }

    public void initializeDirectory(String dir) {
        final Path dirPath = Paths.get(dir);
        if (Files.notExists(dirPath)) {
            createDirectory(dirPath);
        }
    }

    public File[] listDirectories(String path) {
        return new File(path).listFiles(File::isDirectory);
    }

    /**
     * Uploads come in async so it possible for two thread to create a new directory at the same time.
     * Technically creating a new directory a second time is only a problem when there is already content in the dir.
     * Better safe than sorry :)
     */
    synchronized private void createDirectory(Path dirPath) {
        try {
            if (Files.notExists(dirPath)) {
                Files.createDirectory(dirPath);
            }
        } catch (IOException e) {
            throw new FileStorageException("Could not create a new directory for the files", e);
        }
    }
}
