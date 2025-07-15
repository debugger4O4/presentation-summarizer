package ru.debugger4o4.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.RequestSummarizeData;
import ru.debugger4o4.back.service.CraftService;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.service.PresentationSummarizerService;


@Service
public class PresentationSummarizerServiceImpl implements PresentationSummarizerService {

    private GigaChatService gigaChatService;
    private CraftService craftService;

    @Autowired
    public void setGigaChatService(GigaChatService gigaChatService) {
        this.gigaChatService = gigaChatService;
    }

    @Autowired
    public void setCraftService(CraftService craftService) {
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
