package com.example.lonovojvladimir.documentTest.mapper;


import com.example.lonovojvladimir.documentTest.domain.dto.DocumentHistoryDto;
import com.example.lonovojvladimir.documentTest.domain.entity.DocumentHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentHistoryMapper {

    DocumentHistoryEntity toEntity(DocumentHistoryDto documentHistoryDto);
    DocumentHistoryDto toDto(DocumentHistoryEntity documentHistoryEntity);
}
