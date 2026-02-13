package com.example.lonovojvladimir.documentTest.filter;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DocumentStatusFilter implements DocumentFilter {

    @Override
    public boolean isApplicable(DocumentFilterDto filterDto) {
        return filterDto.getStatus() != null;
    }

    @Override
    public Stream<DocumentEntity> apply(Stream<DocumentEntity> documentEntityStream, DocumentFilterDto documentFilterDto) {
        return documentEntityStream.filter(documentEntity -> documentFilterDto.getStatus().equals(documentEntity.getStatus()));
    }
}
