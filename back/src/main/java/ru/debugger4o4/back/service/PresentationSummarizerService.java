package ru.debugger4o4.back.service;

import org.springframework.http.ResponseEntity;
import ru.debugger4o4.back.dto.RequestSummarizeData;

public interface PresentationSummarizerService {

    ResponseEntity<byte[]> getSummarize(RequestSummarizeData requestSummarizeData);
}
