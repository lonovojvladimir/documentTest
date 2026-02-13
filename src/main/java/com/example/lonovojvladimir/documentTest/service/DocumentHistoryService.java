package com.example.lonovojvladimir.documentTest.service;

import com.example.lonovojvladimir.documentTest.domain.dto.DocumentHistoryDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentHistoryEntity;
import com.example.lonovojvladimir.documentTest.mapper.DocumentHistoryMapper;
import com.example.lonovojvladimir.documentTest.repository.DocumentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentHistoryService {

    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentHistoryMapper documentHistoryMapper;

    public List<DocumentHistoryDto> getDocumentHistoryByDocumentId(Long documentId) {
        List<DocumentHistoryEntity> documentHistoryEntityList = documentHistoryRepository.findAllByDocumentIdDocumentId(documentId);
        return documentHistoryEntityList.stream().map(documentHistoryMapper::toDto).collect(Collectors.toList());
    }

    public DocumentHistoryEntity saveDocumentHistory(DocumentHistoryEntity documentHistoryEntity) {
        return documentHistoryRepository.save(documentHistoryEntity);
    }

}
