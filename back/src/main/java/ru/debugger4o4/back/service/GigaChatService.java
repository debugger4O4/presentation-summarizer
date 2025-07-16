package ru.debugger4o4.back.service;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import ru.debugger4o4.back.dto.RequestSummarizeData;

public interface GigaChatService {
    String sendQueryForTechnicalTask(RequestSummarizeData requestSummarizeData);

    void sendQueryToGenerateAndDownloadImage(XMLSlideShow presentation,  XSLFSlide slide, String pictureDescription);

    void getModels();
}
