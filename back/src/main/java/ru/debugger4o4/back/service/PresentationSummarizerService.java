package ru.debugger4o4.back.service;

import org.springframework.http.ResponseEntity;

public interface PresentationSummarizerService {

    ResponseEntity<byte[]> getSummarize(String textForSummarize);
}
