package com.example.lonovojvladimir.documentTest.controller;

import com.example.lonovojvladimir.documentTest.domain.dto.ApproveDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentDto;
import com.example.lonovojvladimir.documentTest.domain.dto.SubmitDocumentsDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionDto;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionHandler;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentResponseDto;
import com.example.lonovojvladimir.documentTest.filter.DocumentFilterDto;
import com.example.lonovojvladimir.documentTest.service.DocumentService;
import com.example.lonovojvladimir.documentTest.validator.DocumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
@DocumentExceptionHandler
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentValidator documentValidator;

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto<DocumentDto>> getDocumentByIdWithHistory(@PathVariable(required = false) Long documentId) {
        return response(documentService.getDocumentByIdWithHistory(documentId), null);
    }

    @GetMapping("/getDocument")
    public ResponseEntity<DocumentResponseDto<Page<DocumentDto>>> getAllDocuments(@RequestParam(required = false) List<Long> documentIds,
                                                                                  @RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam(defaultValue = "created") String sort) {
        return response(documentService.getAllDocuments(documentIds, page, size, sort), null);
    }

    @GetMapping("/allByFilter")
    public ResponseEntity<DocumentResponseDto<List<DocumentDto>>> getAllDocumentsByFilter(@RequestBody(required = false) DocumentFilterDto documentFilterDto) {
        return response(documentService.getAllDocumentsByFilter(documentFilterDto), null);
    }

    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDto<DocumentEntity>> createDocument(@RequestBody DocumentCreateDto documentCreateDto) {
        return response(documentService.createDocument(documentCreateDto), null);
    }

    @PostMapping("/submitDocument")
    public ResponseEntity<DocumentResponseDto<List<Long>>> submitDocument(@RequestBody SubmitDocumentsDto submitDocumentsDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.submitDocument(submitDocumentsDto));
    }

    @PostMapping("/approveDocument")
    public ResponseEntity<DocumentResponseDto<List<Long>>> approveDocument(@RequestBody ApproveDocumentsDto approveDocumentsDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.approveDocument(approveDocumentsDto));
    }

    @PostMapping("/test")
    public ResponseEntity<DocumentResponseDto<List<Long>>> testConcurrency(@RequestBody ApproveDocumentsDto approveDocumentsDto) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ApproveDocumentsDto approveDocumentsDto2 = approveDocumentsDto;
        approveDocumentsDto2.setUsersId(2l);
        executor.submit(() -> documentService.approveDocument(approveDocumentsDto));
        executor.submit(() -> documentService.approveDocument(approveDocumentsDto2));

        executor.shutdown();

        return ResponseEntity.ok().build();
    }

    private <Dto> ResponseEntity<DocumentResponseDto<Dto>> response(Dto dto, List<DocumentExceptionDto> error) {
        DocumentResponseDto<Dto> response = new DocumentResponseDto<>();
        response.setData(dto);
        response.setError(error);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
