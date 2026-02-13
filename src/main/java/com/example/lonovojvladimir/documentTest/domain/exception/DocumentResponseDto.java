package com.example.lonovojvladimir.documentTest.domain.exception;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentResponseDto<T> {

    private T data;
    private List<DocumentExceptionDto> error;

}
