package com.example.lonovojvladimir.documentTest.mapper;

import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import com.example.lonovojvladimir.documentTest.domain.dto.DocumentDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    DocumentEntity toCreateEntity(DocumentCreateDto documentCreateDto);
    DocumentDto toDto(DocumentEntity documentEntity);
    DocumentEntity toEntity(DocumentDto documentDto);


}
