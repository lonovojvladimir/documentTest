package com.example.lonovojvladimir.documentTest.validator;

import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentBusinessException;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionCode;
import com.example.lonovojvladimir.documentTest.domain.exception.DocumentExceptionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UsersValidator {

    public void validate(UsersEntity usersEntity) {
        List<DocumentExceptionDto> exceptions = new ArrayList<>();
        if (usersEntity == null) {
            exceptions.add(DocumentExceptionDto.builder()
                    .code(DocumentExceptionCode.USERNOTFOUND)
                    .property("userId")
                    .build());
        }
        if (!exceptions.isEmpty()) {
            throw new DocumentBusinessException(exceptions);
        }
    }

}
