package com.example.lonovojvladimir.documentTest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRegistryDto {

    private long approvalRegistryId;
    private DocumentDto documentId;
    private UsersDto users;
    private LocalDateTime approved;

}
