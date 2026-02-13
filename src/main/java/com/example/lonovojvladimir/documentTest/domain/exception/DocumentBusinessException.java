package com.example.lonovojvladimir.documentTest.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DocumentBusinessException extends RuntimeException {

    private List<DocumentExceptionDto> errors;

}
