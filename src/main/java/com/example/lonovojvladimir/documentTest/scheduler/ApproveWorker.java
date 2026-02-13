package com.example.lonovojvladimir.documentTest.scheduler;

import com.example.lonovojvladimir.documentTest.domain.dto.ApproveDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import com.example.lonovojvladimir.documentTest.repository.DocumentRepository;
import com.example.lonovojvladimir.documentTest.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApproveWorker {

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    @Value("${workers.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${workers.approve.delay-ms}")
    public void run() {
        long start = System.currentTimeMillis();
        long totalSubmitted = documentRepository.countDocumentEntitiesByStatus(DocumentStatus.SUBMITTED);
        if (totalSubmitted == 0) {
            log.info("APPROVE-WORKER | nothing to process");
            return;
        }
        Pageable pageable = PageRequest.of(0, batchSize);
        Page<DocumentEntity> page = documentRepository.findDocumentEntitiesByStatus(DocumentStatus.SUBMITTED, pageable);
        if (page.isEmpty()) {
            return;
        }
        List<Long> documentIds = page.stream()
                .map(DocumentEntity::getDocumentId)
                .toList();
        log.info("APPROVE-WORKER START | batchSize={} | totalSubmitted={}", documentIds.size(), totalSubmitted);
        try {
            documentService.approveDocument(new ApproveDocumentsDto(documentIds, 1L, "auto-test"));
        } catch (Exception e) {
            log.error("APPROVE-WORKER ERROR", e);
        }
        long remaining = documentRepository.countDocumentEntitiesByStatus(DocumentStatus.SUBMITTED);
        long duration = System.currentTimeMillis() - start;
        log.info("APPROVE-WORKER FINISH | processed={} | remaining={} | durationMs={}", documentIds.size(), remaining, duration);
    }
}
