package com.musiienko.library.controller;

import com.musiienko.library.exception.LibraryException;
import com.musiienko.library.model.ErrorResponse;
import com.musiienko.library.util.LibraryUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.StringJoiner;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LibraryControllerAdvice {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.builder().error("Not found").details("No such resource").build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        StringJoiner joiner = new StringJoiner("; ");
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError && ((FieldError) error).getField().equals("category")) {
                joiner.add(error.getDefaultMessage() + LibraryUtil.categoriesListing());
            } else {
                joiner.add(error.getDefaultMessage());
            }
        }
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder().error("Validation").details(joiner.toString()).build());
    }

    @ExceptionHandler(LibraryException.class)
    public ResponseEntity<?> handleLibraryException(LibraryException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder().error("Data violation").id(e.getBookId()).details(e.getDetails()).build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleBadCredentials(AuthenticationException e) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResponse.builder().error("Bad credentials").details("Credentials either do not exist or do not match")
                        .build());
    }

}
