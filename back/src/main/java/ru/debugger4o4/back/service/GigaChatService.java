package ru.debugger4o4.back.service;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import ru.debugger4o4.back.dto.RequestSummarizeData;

public interface GigaChatService {
    String sendQueryForTechnicalTask(RequestSummarizeData requestSummarizeData);

    void sendQueryToGenerateAndDownloadImage(XSLFSlide slide, String imageDescription);

    void getModels();

    String getToken();
}
