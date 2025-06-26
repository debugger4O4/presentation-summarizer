package ru.debugger4o4.back.service;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface PresentationSummarizerService {

    ResponseEntity<InputStreamResource> getSummarize(String textForSummarize);
}
