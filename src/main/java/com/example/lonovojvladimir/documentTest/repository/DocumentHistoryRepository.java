package com.example.lonovojvladimir.documentTest.repository;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DocumentHistoryRepository extends PagingAndSortingRepository<DocumentHistoryEntity, Long>, CrudRepository<DocumentHistoryEntity, Long> {
    List<DocumentHistoryEntity> findAllByDocumentIdDocumentId(Long documentId);
}
