package com.example.lonovojvladimir.documentTest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private long usersId;
    private String login;
    private String lastName;
    private String firstName;
    private String middleName;
}
