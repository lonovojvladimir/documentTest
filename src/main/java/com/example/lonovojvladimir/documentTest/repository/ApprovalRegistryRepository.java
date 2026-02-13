package com.example.lonovojvladimir.documentTest.repository;

import com.example.lonovojvladimir.documentTest.domain.entity.ApprovalRegistryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApprovalRegistryRepository extends PagingAndSortingRepository<ApprovalRegistryEntity, Long>, CrudRepository<ApprovalRegistryEntity, Long> {
}
