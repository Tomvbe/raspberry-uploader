package com.raspberry.uploader.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String msg, Exception e) {
        super(msg, e);
    }

    public FileStorageException(String msg) {
        super(msg);
    }
}
