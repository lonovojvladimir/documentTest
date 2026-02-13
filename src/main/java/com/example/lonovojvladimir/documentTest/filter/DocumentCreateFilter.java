package com.example.lonovojvladimir.documentTest.filter;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;

import java.util.stream.Stream;

public class DocumentCreateFilter implements DocumentFilter {

    @Override
    public boolean isApplicable(DocumentFilterDto filterDto) {
        return filterDto.getCreated()!=null;
    }

    @Override
    public Stream<DocumentEntity> apply(Stream<DocumentEntity> documentEntityStream, DocumentFilterDto documentFilterDto) {
        return documentEntityStream.filter(documentEntity -> documentEntity.getCreated().isAfter(documentFilterDto.getCreated()));
    }
}
