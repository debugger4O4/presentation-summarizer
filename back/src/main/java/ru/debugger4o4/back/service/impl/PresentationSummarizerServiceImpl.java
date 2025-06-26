package ru.debugger4o4.back.service.impl;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.debugger4o4.back.service.PresentationSummarizerService;
import org.springframework.http.MediaType;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Component
public class PresentationSummarizerServiceImpl implements PresentationSummarizerService {

    private final Logger logger = LoggerFactory.getLogger(PresentationSummarizerServiceImpl.class);

    @Override
    public ResponseEntity<InputStreamResource> getSummarize(String textForSummarize) {
        System.out.println(textForSummarize);
        XMLSlideShow summarizePptx = makePresentation(textForSummarize); // TODO метод отправки текста в гигу для суммаризации и формирование файла.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Objects.requireNonNull(summarizePptx).write(out);
        } catch (IOException e) {
            logger.error("PresentationSummarizerServiceImpl IOException in getSummarize: {}", e.getMessage());
        }
        byte[] data = out.toByteArray();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "filename" + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(stream));
    }

    // TODO метод отправки текста в гигу для суммаризации и формирование файла.
    private XMLSlideShow makePresentation(String textForSummarize) {
        XSLFSlide slide;
        File image;
        byte[] picture;
        try (XMLSlideShow pptx = new XMLSlideShow();
             FileOutputStream out = new FileOutputStream("summarize_presentation/summarize_presentation.pptx")) {
            for (int i = 1; i <= 10; i++) {
                slide = pptx.createSlide();
                image = new File("screens/slide_" + i + ".png");
                picture = IOUtils.toByteArray(new FileInputStream(image));
                XSLFPictureData idx = pptx.addPicture(picture, PictureData.PictureType.PNG);
                XSLFPictureShape pic = slide.createPicture(idx);
                pic.setAnchor(new Rectangle(0, 0, 1024, 540));
            }
            pptx.write(out);
            return pptx;
        } catch (Exception e) {
            logger.error("PresentationSummarizerServiceImpl Exception in makePresentation: {}", e.getMessage());
        }
        return null;
    }
}
