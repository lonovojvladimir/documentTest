package com.example.lonovojvladimir.documentTest.utility;

import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DocumentApiClient {

    private final RestTemplate restTemplate;

    @Value("${generator.api.base-url}")
    private String baseUrl;

    public void create(DocumentCreateDto request) {
        restTemplate.postForEntity(
                baseUrl + "/create",
                request,
                Void.class
        );
    }
}