package com.example.lonovojvladimir.documentTest;

import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentDto;
import com.example.lonovojvladimir.documentTest.domain.dto.SubmitDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.dto.UsersDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentBusinessException;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionDto;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentResponseDto;
import com.example.lonovojvladimir.documentTest.filter.DocumentFilter;
import com.example.lonovojvladimir.documentTest.mapper.DocumentMapper;
import com.example.lonovojvladimir.documentTest.repository.DocumentRepository;
import com.example.lonovojvladimir.documentTest.service.ApprovalRegistryService;
import com.example.lonovojvladimir.documentTest.service.DocumentHistoryService;
import com.example.lonovojvladimir.documentTest.service.DocumentService;
import com.example.lonovojvladimir.documentTest.service.UsersService;
import com.example.lonovojvladimir.documentTest.util.DocumentNumberGenerator;
import com.example.lonovojvladimir.documentTest.validator.DocumentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentNumberGenerator documentNumberGenerator;
    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private UsersService usersService;
    @Mock
    private DocumentHistoryService documentHistoryService;
    @Mock
    private DocumentValidator documentValidator;
    @Mock
    private ApprovalRegistryService approvalRegistryService;
    @Mock
    private List<DocumentFilter> documentFilters;
    @Spy
    @InjectMocks
    private DocumentService documentService;

    @Test
    void shouldReturnDocumentWithHistory() {
        Long documentId = 1L;
        DocumentEntity entity = new DocumentEntity();
        DocumentDto dto = new DocumentDto();
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(entity));
        when(documentMapper.toDto(entity)).thenReturn(dto);
        when(documentHistoryService.getDocumentHistoryByDocumentId(documentId)).thenReturn(List.of());
        DocumentDto result = documentService.getDocumentByIdWithHistory(documentId);
        assertNotNull(result);
        verify(documentValidator).validate(entity);
        verify(documentMapper).toDto(entity);
        verify(documentHistoryService).getDocumentHistoryByDocumentId(documentId);
    }

    @Test
    void shouldCreateDocument() {
        UsersEntity user = new UsersEntity();
        user.setUsersId(1L);
        DocumentCreateDto createDto = new DocumentCreateDto(new UsersDto(), "test");
        DocumentEntity mapped = new DocumentEntity();
        when(documentMapper.toCreateEntity(createDto)).thenReturn(mapped);
        when(documentNumberGenerator.documentNumberGenerator()).thenReturn("DOC-1");
        when(usersService.findById(any())).thenReturn(user);
        when(documentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        DocumentEntity result = documentService.createDocument(createDto);
        assertEquals(DocumentStatus.DRAFT, result.getStatus());
        assertEquals("DOC-1", result.getDocNumber());
        verify(documentRepository).save(mapped);
    }

    @Test
    void shouldSubmitDocumentsWithPartialErrors() {
        SubmitDocumentsDto dto = new SubmitDocumentsDto(List.of(1L, 2L), 10L, "comment");
        UsersEntity user = new UsersEntity();
        when(usersService.findById(10L)).thenReturn(user);
        doNothing().when(documentService).submitSingle(1L, user, "comment");
        doThrow(new DocumentBusinessException(List.of(new DocumentExceptionDto()))).when(documentService)
                .submitSingle(2L, user, "comment");
        DocumentResponseDto<List<Long>> response = documentService.submitDocument(dto);
        assertEquals(1, response.getData().size());
        assertEquals(1L, response.getData().get(0));
        assertEquals(1, response.getError().size());
    }

    @Test
    void shouldThrowConflictOnOptimisticLock() {
        Long documentId = 1L;
        UsersEntity user = new UsersEntity();
        DocumentEntity entity = new DocumentEntity();
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(entity));
        doNothing().when(documentValidator).validateApprove(entity);
        when(documentRepository.save(any())).thenThrow(ObjectOptimisticLockingFailureException.class);
        assertThrows(DocumentBusinessException.class,
                () -> documentService.approveSingle(documentId, user, "comment"));
    }

}
