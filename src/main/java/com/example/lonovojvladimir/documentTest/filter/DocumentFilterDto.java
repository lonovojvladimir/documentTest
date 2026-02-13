package com.example.lonovojvladimir.documentTest.filter;

import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentFilterDto {

    private String status;
    private UsersEntity users;
    private LocalDateTime created;
    private LocalDateTime updated;
}
