package com.example.lonovojvladimir.documentTest.validator;

import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentBusinessException;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionCode;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DocumentValidator {

    public void validate(DocumentEntity documentEntity) {
        List<DocumentExceptionDto> exceptions = new ArrayList<>();
        if (documentEntity == null) {
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.DOCUMENTNOTFOUND)
                    .property("Document not found")
                    .build());
        }
        if (!exceptions.isEmpty()) {
            throw new DocumentBusinessException(exceptions);
        }
    }

    public void validateSubmit(DocumentEntity documentEntity) {
        List<DocumentExceptionDto> exceptions = new ArrayList<>();
        if (documentEntity == null) {
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.DOCUMENTNOTFOUND)
                    .property("DocumentEntityNull " + documentEntity.getDocumentId())
                    .build());
        } else {
            if (!documentEntity.getStatus().equals(DocumentStatus.DRAFT)) {
                exceptions.add(DocumentExceptionDto.builder()
                        .code(DocumentExceptionCode.BADSTATUS)
                        .property("draftStatus " + documentEntity.getDocumentId())
                        .build());
            }
        }
        if (!exceptions.isEmpty()) {
            throw new DocumentBusinessException(exceptions);
        }
    }

    public void validateApprove(DocumentEntity documentEntity) {
        List<DocumentExceptionDto> exceptions = new ArrayList<>();
        if (documentEntity == null) {
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.DOCUMENTNOTFOUND)
                    .property("DocumentEntityNull " + documentEntity.getDocumentId())
                    .build());
        } else {
            if (!documentEntity.getStatus().equals(DocumentStatus.SUBMITTED)) {
                exceptions.add(DocumentExceptionDto.builder()
                        .code(DocumentExceptionCode.BADSTATUS)
                        .property("sumbitedStatus " + documentEntity.getDocumentId())
                        .build());
            }
        }
        if (!exceptions.isEmpty()) {
            throw new DocumentBusinessException(exceptions);
        }
    }





}
