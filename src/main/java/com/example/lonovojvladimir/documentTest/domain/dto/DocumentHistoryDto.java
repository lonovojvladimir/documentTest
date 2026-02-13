package com.example.lonovojvladimir.documentTest.domain.dto;

import com.example.lonovojvladimir.documentTest.domain.enums.DocumentAction;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentHistoryDto {

    private long documentHistoryId;
    private DocumentDto documentId;
    @Enumerated(EnumType.STRING)
    private DocumentAction action;
    @Enumerated(EnumType.STRING)
    private DocumentStatus fromStatus;
    @Enumerated(EnumType.STRING)
    private DocumentStatus toStatus;
    private UsersDto users;
    private String comment;
    private LocalDateTime created;

}
