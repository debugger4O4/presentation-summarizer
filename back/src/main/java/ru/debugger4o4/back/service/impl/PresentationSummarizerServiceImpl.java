package ru.debugger4o4.back.service.impl;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.debugger4o4.back.service.PresentationSummarizerService;

@Component
public class PresentationSummarizerServiceImpl implements PresentationSummarizerService {

    private final Logger logger = LoggerFactory.getLogger(PresentationSummarizerServiceImpl.class);

    @Override
    public ResponseEntity<byte[]> getSummarize(String textForSummarize) {
        System.out.println(textForSummarize);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XMLSlideShow presentation = new XMLSlideShow()) {
            XSLFSlide slide = presentation.createSlide();
            XSLFTextBox shape = slide.createTextBox();
            XSLFTextParagraph p = shape.addNewTextParagraph();
            XSLFTextRun r = p.addNewTextRun();
            r.setText(textForSummarize);
            presentation.write(outputStream);
        } catch (IOException e) {
            logger.error("PresentationSummarizerServiceImpl Exception in getSummarize: {}", e.getMessage());
        }
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
