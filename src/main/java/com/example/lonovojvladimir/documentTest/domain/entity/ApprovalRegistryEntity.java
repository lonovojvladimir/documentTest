package com.example.lonovojvladimir.documentTest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "approval_registry")
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRegistryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approval_registry_seq")
    @SequenceGenerator(name = "approval_registry_seq", sequenceName = "approval_registry_seq", allocationSize = 1)
    @Column(name = "approval_registry_id")
    private long approvalRegistryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity documentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UsersEntity users;
    @Column(name = "approved")
    private LocalDateTime approved;
}
