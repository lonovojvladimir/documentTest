package com.example.lonovojvladimir.documentTest.utility;

import com.example.lonovojvladimir.documentTest.domain.dto.DocumentCreateDto;
import com.example.lonovojvladimir.documentTest.domain.dto.UsersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentGeneratorService {

    private final DocumentApiClient apiClient;

    @Value("${generator.count}")
    private int count;

    @Value("${generator.users-id}")
    private Long userId;

    public void generate() {
        long start = System.currentTimeMillis();
        log.info("GENERATOR START | total={}", count);
        for (int i = 1; i <= count; i++) {
            DocumentCreateDto request = new DocumentCreateDto();
            UsersDto usersDto = new UsersDto();
            usersDto.setUsersId(userId);
            request.setUsers(usersDto);
            request.setTitle("test " + i);
            long singleStart = System.currentTimeMillis();
            try {
                apiClient.create(request);
                long duration = System.currentTimeMillis() - singleStart;
                log.info("GENERATOR PROGRESS | created={} of {} | lastDurationMs={}", i, count, duration);
            } catch (HttpClientErrorException e) {
                log.error("GENERATOR ERROR | index={}", i, e);
            }
        }
        long totalDuration = System.currentTimeMillis() - start;
        log.info("GENERATOR FINISH | total={} | durationMs={}", count, totalDuration);
    }
}
