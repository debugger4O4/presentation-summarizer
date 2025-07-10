package ru.debugger4o4.back.service;

import ru.debugger4o4.back.dto.RequestSummarizeData;

public interface GigaChatService {
    String sendQuery(RequestSummarizeData requestSummarizeData);

    void getModels();

    String getToken();
}
