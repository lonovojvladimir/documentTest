package com.example.lonovojvladimir.documentTest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
public class ApproveDocumentsDto {

    @NotNull
    private List<Long> documentIds;
    @NotNull
    private Long usersId;
    private String comment;

}
