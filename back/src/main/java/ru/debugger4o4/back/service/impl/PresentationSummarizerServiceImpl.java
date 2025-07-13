package ru.debugger4o4.back.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.RequestSummarizeData;
import ru.debugger4o4.back.service.CraftService;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.service.PresentationSummarizerService;


@Service
public class PresentationSummarizerServiceImpl implements PresentationSummarizerService {

    private final GigaChatService gigaChatService;
    private final CraftService craftService;

    private final Logger logger = LoggerFactory.getLogger(PresentationSummarizerServiceImpl.class);

    public PresentationSummarizerServiceImpl(GigaChatService gigaChatService, CraftService craftService) {
        this.gigaChatService = gigaChatService;
        this.craftService = craftService;
    }

    @Override
    public ResponseEntity<byte[]> getSummarize(RequestSummarizeData requestSummarizeData) {
        String technicalTask = gigaChatService.sendQueryForTechnicalTask(requestSummarizeData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        craftService.completeTechnicalTask(technicalTask, outputStream);
        byte[] bytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=summarize.pptx");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }
}
