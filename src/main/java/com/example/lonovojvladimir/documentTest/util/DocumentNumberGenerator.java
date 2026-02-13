package com.example.lonovojvladimir.documentTest.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class DocumentNumberGenerator {

    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");

    private static final String PREFIX = "DOC";

    public String documentNumberGenerator() {
        String timestamp = LocalDateTime.now().format(DATE_TIME);
        return PREFIX + "-" + timestamp;
    }

}
