package com.raspberry.uploader.controller;

import com.raspberry.uploader.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public void handleException(FileStorageException exception) {
        throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
    }
}
