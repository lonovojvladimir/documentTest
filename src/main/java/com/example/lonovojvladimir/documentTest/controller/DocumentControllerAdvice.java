package com.example.lonovojvladimir.documentTest.controller;

import com.example.lonovojvladimir.documentTest.domain.exception.DocumentBusinessException;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionHandler;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = DocumentExceptionHandler.class)
public class DocumentControllerAdvice {

    @ExceptionHandler(DocumentBusinessException.class)
    public ResponseEntity<DocumentResponseDto<?>> handleException(DocumentBusinessException e) {
        return new ResponseEntity<>(DocumentResponseDto.builder()
                .data(null)
                .error(e.getErrors())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
