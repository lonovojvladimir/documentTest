package com.example.lonovojvladimir.documentTest.domain.dto;

import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    private long documentId;
    private String docNumber;
    private UsersDto users;
    private String title;
    private DocumentStatus status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private long version;
    private List<DocumentHistoryDto> history;

}
