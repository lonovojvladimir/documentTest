package com.example.lonovojvladimir.documentTest.domain.entity;

import com.example.lonovojvladimir.documentTest.domain.enums.DocumentAction;
import com.example.lonovojvladimir.documentTest.domain.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "document_history")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_history_seq")
    @SequenceGenerator(name = "document_history_seq", sequenceName = "document_history_seq", allocationSize = 1)
    @Column(name = "document_history_id")
    private long documentHistoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity documentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private DocumentAction action;
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private DocumentStatus fromStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status")
    private DocumentStatus toStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UsersEntity users;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created")
    private LocalDateTime created;
}
