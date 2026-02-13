package com.example.lonovojvladimir.documentTest.service;

import com.example.lonovojvladimir.documentTest.domain.entity.ApprovalRegistryEntity;
import com.example.lonovojvladimir.documentTest.repository.ApprovalRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovalRegistryService {

    private final ApprovalRegistryRepository approvalRegistryRepository;

    public ApprovalRegistryEntity save(ApprovalRegistryEntity approvalRegistryEntity) {
        return approvalRegistryRepository.save(approvalRegistryEntity);
    }

}
