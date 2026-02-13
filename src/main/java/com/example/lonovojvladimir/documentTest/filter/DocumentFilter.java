package com.example.lonovojvladimir.documentTest.filter;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;

import java.util.stream.Stream;

public interface DocumentFilter {

    boolean isApplicable(DocumentFilterDto filterDto);
    Stream<DocumentEntity> apply(Stream<DocumentEntity> documentEntityStream, DocumentFilterDto documentFilterDto);

}
