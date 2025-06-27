package ru.debugger4o4.back.service.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
//import org.apache.poi.xslf.usermodel.XSLFTextBody;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.sl.usermodel.ShapeType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XMLSlideShow presentation = new XMLSlideShow()) {
            XSLFSlide slide = presentation.createSlide();
            XSLFShape shape = slide.createAutoShape();
//            shape.setShapeType(ShapeType.RECT);
//            shape.setAnchor(new Rectangle2D.Double(100, 100, 500, 300));
//            XSLFTextBody textBody = shape.getTextBody();
//            if (textBody == null) {
//                textBody = shape.addTextBody();
//            }
//            XSLFTextParagraph para = textBody.addNewTextParagraph();
//            XSLFTextRun textRun = para.addNewTextRun();
//            textRun.setText(textForSummarize);
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
