package com.example.lonovojvladimir.documentTest.domain.exception;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentExceptionDto {

    private String property;
    private DocumentExceptionCode code;

}
