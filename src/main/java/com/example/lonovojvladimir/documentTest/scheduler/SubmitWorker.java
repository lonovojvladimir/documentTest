package com.example.lonovojvladimir.documentTest.scheduler;

import com.example.lonovojvladimir.documentTest.domain.dto.SubmitDocumentsDto;
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
public class SubmitWorker {

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    @Value("${workers.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${workers.submit.delay-ms}")
    public void run() {
        long start = System.currentTimeMillis();
        long totalDraft = documentRepository.countDocumentEntitiesByStatus(DocumentStatus.DRAFT);
        if (totalDraft == 0) {
            log.info("SUBMIT-WORKER | nothing to process");
            return;
        }
        Pageable pageable = PageRequest.of(0, batchSize);
        Page<DocumentEntity> page =
                documentRepository.findDocumentEntitiesByStatus(
                        DocumentStatus.DRAFT,
                        pageable
                );
        List<Long> documentIds = page.stream()
                .map(DocumentEntity::getDocumentId)
                .toList();
        log.info("SUBMIT-WORKER START | batchSize={} | totalDraft={}", documentIds.size(), totalDraft);
        try {
            documentService.submitDocument(new SubmitDocumentsDto(documentIds, 1L, "auto-test"));
        } catch (Exception e) {
            log.error("SUBMIT-WORKER ERROR", e);
        }
        long remaining = documentRepository.countDocumentEntitiesByStatus(DocumentStatus.DRAFT);
        long duration = System.currentTimeMillis() - start;
        log.info("SUBMIT-WORKER FINISH | processed={} | remaining={} | durationMs={}", documentIds.size(), remaining, duration);
    }

}
