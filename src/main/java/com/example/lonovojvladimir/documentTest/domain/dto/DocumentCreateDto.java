package com.example.lonovojvladimir.documentTest.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreateDto {
    private UsersDto users;
    @NonNull
    private String title;

}
