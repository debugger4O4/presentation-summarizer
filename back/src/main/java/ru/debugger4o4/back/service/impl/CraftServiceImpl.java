package ru.debugger4o4.back.service.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.SlideData;
import ru.debugger4o4.back.service.CraftService;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.util.Util;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
public class CraftServiceImpl implements CraftService {


    private GigaChatService gigaChatService;

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    @Autowired
    public void setGigaChatService(GigaChatService gigaChatService) {
        this.gigaChatService = gigaChatService;
    }

    @Override
    public void completeTechnicalTask(String technicalTask, ByteArrayOutputStream outputStream) {
        String[] slides = Arrays
                .stream(technicalTask.split("(\\*\\*Слайд \\d\\*\\*)|(\\*\\*Слайд\\*\\*)"))
                .filter(f -> !f.isEmpty())
                .map(String::trim)
                .toArray(String[]::new);
        createPresentation(slides, outputStream);
    }

    private void createPresentation(String[] slides, ByteArrayOutputStream outputStream) {

        try (
                XMLSlideShow presentation = new XMLSlideShow();
                // Для тестов.
//                FileOutputStream out = new FileOutputStream("powerpoint.pptx")
        ) {
            for (String slideContent : slides) {
                XSLFSlide slide = presentation.createSlide();
                XSLFTextBox shape = slide.createTextBox();
                shape.setAnchor(new Rectangle(50, 50, 600, 300));
                XSLFTextParagraph titleParagraph = shape.addNewTextParagraph();
                XSLFTextParagraph textParagraph = shape.addNewTextParagraph();
                XSLFTextRun title = titleParagraph.addNewTextRun();
                XSLFTextRun text = textParagraph.addNewTextRun();
                SlideData slideData = extractSlideData(slideContent);
                title.setText(slideData.getTitle());
                title.setFontSize(36.0);
                text.setText(
                        slideData.getText() + "\n" +
                        slideData.getStages() + "\n" +
                        slideData.getReasons()
                );
                text.setFontSize(18.0);
                if (!slideData.getImage().isEmpty()) {
                    gigaChatService.sendQueryToGenerateAndDownloadImage(presentation, slide, slideData.getImage());
                } else if (!slideData.getStatistic().isEmpty()) {
                    gigaChatService.sendQueryToGenerateAndDownloadImage(presentation, slide, slideData.getStatistic());
                } else if (!slideData.getGraphic().isEmpty()) {
                    gigaChatService.sendQueryToGenerateAndDownloadImage(presentation, slide, slideData.getGraphic());
                }
            }
            presentation.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            logger.error("CraftServiceImpl exception on createPresentation(): {}", e.getMessage());
        }
    }

    private SlideData extractSlideData(String slideContent) {
        SlideData slide = new SlideData();
        String[] blocks = Arrays.stream(slideContent.split("\\*\\*")).filter(f -> !f.isEmpty()).toArray(String[]::new);
        Map<String, String> content = new HashMap<>();
        for (int i = 1; i < blocks.length; i += 2) {
            String key = cleanText(blocks[i - 1]).replaceAll(":", "");
            String value = cleanText(blocks[i]);
            if (!key.isEmpty() && !value.isEmpty()) {
                content.put(key, value);
            }
        }
        slide.setTitle(content.getOrDefault("Заголовок", ""));

        slide.setText(content.getOrDefault("Текст", ""));
        slide.setStages(content.getOrDefault("Причины", ""));
        slide.setReasons(content.getOrDefault("Этапы", ""));

        slide.setImage(content.getOrDefault("Картинка", ""));
        slide.setGraphic(content.getOrDefault("График", ""));
        slide.setStatistic(content.getOrDefault("Статистика", ""));
        return slide;
    }

    private String cleanText(String text) {
        return text
                .replace("\n\n", "\n")
                .replace("\r", "")
                .replaceAll("^\\s+|\\s+$", "")
                .replaceAll("\\s+", " ")
                .replaceAll("\n\\s+", "\n")
                .trim();
    }
}


