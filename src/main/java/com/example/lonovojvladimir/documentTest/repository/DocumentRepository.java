package com.example.lonovojvladimir.documentTest.repository;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DocumentRepository extends PagingAndSortingRepository<DocumentEntity, Long>, CrudRepository<DocumentEntity, Long> {

    Page<DocumentEntity> findDocumentEntitiesByDocumentIdIn(List<Long> documentIds, Pageable pageable);

    Page<DocumentEntity> findDocumentEntitiesByStatus(DocumentStatus status, Pageable pageable);

    long countDocumentEntitiesByStatus(DocumentStatus status);

}
