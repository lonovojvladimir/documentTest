package com.example.lonovojvladimir.documentTest.service;

import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import com.example.lonovojvladimir.documentTest.repository.UsersRepository;
import com.example.lonovojvladimir.documentTest.validator.UsersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersValidator usersValidator;
    public UsersEntity findById(Long id) {
        UsersEntity usersEntity = usersRepository.findById(id).orElse(null);
        usersValidator.validate(usersEntity);
        return usersEntity;
    }

}
