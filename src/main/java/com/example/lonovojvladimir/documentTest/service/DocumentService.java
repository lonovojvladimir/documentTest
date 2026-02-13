package com.example.lonovojvladimir.documentTest.service;

import com.example.lonovojvladimir.documentTest.domain.dto.ApproveDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentDto;
import com.example.lonovojvladimir.documentTest.domain.dto.SubmitDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.entity.ApprovalRegistryEntity;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentHistoryEntity;
import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentAction;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentBusinessException;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionCode;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionDto;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentResponseDto;
import com.example.lonovojvladimir.documentTest.filter.DocumentFilter;
import com.example.lonovojvladimir.documentTest.filter.DocumentFilterDto;
import com.example.lonovojvladimir.documentTest.mapper.DocumentMapper;
import com.example.lonovojvladimir.documentTest.repository.DocumentRepository;
import com.example.lonovojvladimir.documentTest.util.DocumentNumberGenerator;

import com.example.lonovojvladimir.documentTest.validator.DocumentValidator;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentNumberGenerator documentNumberGenerator;
    private final DocumentMapper documentMapper;
    private final UsersService usersService;
    private final DocumentHistoryService documentHistoryService;
    private final DocumentValidator documentValidator;
    private final ApprovalRegistryService approvalRegistryService;
    private final List<DocumentFilter> documentFilters;

    public DocumentDto getDocumentByIdWithHistory(Long documentId) {
        DocumentEntity documentEntity = documentRepository.findById(documentId).orElse(null);
        documentValidator.validate(documentEntity);
        DocumentDto documentDto = documentMapper.toDto(documentEntity);
        documentDto.setHistory(documentHistoryService.getDocumentHistoryByDocumentId(documentId));
        return documentDto;
    }

    public Page<DocumentDto> getAllDocuments(List<Long> documentIds, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sort));
        Page<DocumentEntity> documentEntityPage = null;
        if (documentIds == null || documentIds.isEmpty()) {
            documentEntityPage = documentRepository.findAll(pageable);
        } else {
            documentEntityPage = documentRepository.findDocumentEntitiesByDocumentIdIn(documentIds, pageable);
        }
        return documentEntityPage.map(documentMapper::toDto);
    }

    public List<DocumentDto> getAllDocumentsByFilter(DocumentFilterDto documentFilterDto) {
        Stream<DocumentEntity> documentEntityStream = StreamSupport.stream(documentRepository.findAll().spliterator(), false);
        for (DocumentFilter documentFilter : documentFilters) {
            if (documentFilter.isApplicable(documentFilterDto)) {
                documentEntityStream = documentFilter.apply(documentEntityStream, documentFilterDto);
            }
        }
        return documentEntityStream.map(documentMapper::toDto).toList();
    }

    public DocumentEntity createDocument(DocumentCreateDto documentCreateDto) {
        LocalDateTime now = LocalDateTime.now();
        DocumentEntity documentEntity = documentMapper.toCreateEntity(documentCreateDto);
        documentEntity.setDocNumber(documentNumberGenerator.documentNumberGenerator());
        documentEntity.setUsers(usersService.findById(documentCreateDto.getUsers().getUsersId()));
        documentEntity.setStatus(DocumentStatus.DRAFT);
        documentEntity.setCreated(now);
        documentEntity.setUpdated(now);
        documentEntity.setVersion(1);
        return documentRepository.save(documentEntity);
    }

    public DocumentResponseDto<List<Long>> submitDocument(SubmitDocumentsDto submitDocumentsDto) {
        UsersEntity initiator = usersService.findById(submitDocumentsDto.getUsersId());
        List<Long> successIds = new ArrayList<>();
        List<DocumentExceptionDto> documentExceptionDtoList = new ArrayList<>();
        for (Long documentId : submitDocumentsDto.getDocumentIds()) {
            try {
                submitSingle(documentId, initiator, submitDocumentsDto.getComment());
                successIds.add(documentId);
            } catch (DocumentBusinessException e) {
                documentExceptionDtoList.addAll(e.getErrors());
            }
        }
        DocumentResponseDto<List<Long>> response = new DocumentResponseDto<>();
        response.setData(successIds);
        response.setError(documentExceptionDtoList);
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void submitSingle(Long documentId, UsersEntity usersId, String comment) {
        DocumentEntity document = documentRepository.findById(documentId).orElse(null);
        documentValidator.validateSubmit(document);
        document.setStatus(DocumentStatus.SUBMITTED);
        document.setUpdated(LocalDateTime.now());
        DocumentHistoryEntity history = new DocumentHistoryEntity();
        history.setDocumentId(document);
        history.setAction(DocumentAction.SUBMIT);
        history.setFromStatus(DocumentStatus.DRAFT);
        history.setToStatus(DocumentStatus.SUBMITTED);
        history.setUsers(usersId);
        history.setComment(comment);
        history.setCreated(LocalDateTime.now());
        try {
            documentRepository.save(document);
            documentHistoryService.saveDocumentHistory(history);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            List<DocumentExceptionDto> exceptions = new ArrayList<>();
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.CONFLICT)
                    .property("Conflict")
                    .build());
            throw new DocumentBusinessException(exceptions);
        }
    }

    public DocumentResponseDto<List<Long>> approveDocument(ApproveDocumentsDto approveDocumentsDto) {
        UsersEntity initiator = usersService.findById(approveDocumentsDto.getUsersId());
        List<Long> successIds = new ArrayList<>();
        List<DocumentExceptionDto> documentExceptionDtoList = new ArrayList<>();
        for (Long documentId : approveDocumentsDto.getDocumentIds()) {
            try {
                approveSingle(documentId, initiator, approveDocumentsDto.getComment());
                successIds.add(documentId);

            } catch (DocumentBusinessException e) {
                documentExceptionDtoList.addAll(e.getErrors());
            }
        }
        DocumentResponseDto<List<Long>> response = new DocumentResponseDto<>();
        response.setData(successIds);
        response.setError(documentExceptionDtoList);
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void approveSingle(Long documentId, UsersEntity usersId, String comment) {
        DocumentEntity document = documentRepository.findById(documentId).orElse(null);
        documentValidator.validateApprove(document);
        document.setStatus(DocumentStatus.APPROVED);
        document.setUpdated(LocalDateTime.now());
        DocumentHistoryEntity history = new DocumentHistoryEntity();
        history.setDocumentId(document);
        history.setAction(DocumentAction.APPROVE);
        history.setFromStatus(DocumentStatus.SUBMITTED);
        history.setToStatus(DocumentStatus.APPROVED);
        history.setUsers(usersId);
        history.setComment(comment);
        history.setCreated(LocalDateTime.now());
        ApprovalRegistryEntity approvalRegistryEntity = new ApprovalRegistryEntity();
        approvalRegistryEntity.setDocumentId(document);
        approvalRegistryEntity.setUsers(usersId);
        approvalRegistryEntity.setApproved(LocalDateTime.now());
        try {
            documentRepository.save(document);
            documentHistoryService.saveDocumentHistory(history);
            approvalRegistryService.save(approvalRegistryEntity);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            List<DocumentExceptionDto> exceptions = new ArrayList<>();
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.CONFLICT)
                    .property("Conflict " + documentId)
                    .build());
            throw new DocumentBusinessException(exceptions);
        }
    }

}
