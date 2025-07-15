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
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                .stream(technicalTask.split("(\\*\\*Слайд )"))
                .filter(f -> !f.isEmpty())
                .map(String::trim)
                .toArray(String[]::new);
        createPresentation(slides, outputStream);
    }

    private void createPresentation(String[] slides, ByteArrayOutputStream outputStream) {

        try (
                XMLSlideShow presentation = new XMLSlideShow();
                // Для тестов.
                FileOutputStream out = new FileOutputStream("powerpoint.pptx")
        ) {
            for (String slideContent : slides) {
                XSLFSlide slide = presentation.createSlide();
                XSLFTextBox shape = slide.createTextBox();
                shape.setAnchor(new Rectangle(50, 50, 600, 300));
                XSLFTextParagraph titleParagraph = shape.addNewTextParagraph();
                XSLFTextParagraph subTitleParagraph = shape.addNewTextParagraph();
                XSLFTextParagraph textParagraph = shape.addNewTextParagraph();
                XSLFTextRun title = titleParagraph.addNewTextRun();
                XSLFTextRun subTitle = subTitleParagraph.addNewTextRun();
                XSLFTextRun text = textParagraph.addNewTextRun();
                SlideData slideData = extractSlideData(slideContent);
                title.setText(slideData.getTitle());
                title.setFontSize(36.0);
                subTitle.setText(slideData.getSubtitle());
                subTitle.setFontSize(24.0);
                text.setText(slideData.getText());
                text.setFontSize(12.0);
                String imageDescription = slideData.getImage();
//                if (imageDescription != null && !imageDescription.isEmpty()) {
//                    gigaChatService.sendQueryToGenerateAndDownloadImage(slide, imageDescription);
//                }
            }
            presentation.write(out);
        } catch (Exception e) {
            logger.error("CraftServiceImpl exception on createPresentation(): {}", e.getMessage());
        }
    }

    private SlideData extractSlideData(String slideContent) {
        SlideData data = new SlideData();

        Pattern titlePattern = Pattern.compile("\\*\\*Название:\\*\\*([\\s\\S]*?)\\*\\*Подзаголовок:\\*\\*([\\s\\S]*?)\\*\\*Текст:\\*\\*");
        Matcher matcher = titlePattern.matcher(slideContent);
        if (matcher.find()) {
            String title = matcher.group(1).trim();
            String subtitle = matcher.group(2).trim();
            data.setTitle(title);
            data.setSubtitle(subtitle);
        }

        Pattern textPattern = Pattern.compile("\\*\\*Текст:\\*\\*([\\s\\S]*?)(?:\\*\\*(?:Картинка:\\*\\*|График:\\*\\*|Статистика:\\*\\*)|$)");
        matcher = textPattern.matcher(slideContent);
        if (matcher.find()) {
            String text = matcher.group(1).trim();
            data.setText(text);
        }

        Pattern imagePattern = Pattern.compile("\\*\\*(?:Картинка:\\*\\*|График:\\*\\*|Статистика:\\*\\*)([\\s\\S]*?)(?:\\*\\*|$)");
        matcher = imagePattern.matcher(slideContent);
        if (matcher.find()) {
            String image = matcher.group(1).trim();
            data.setImage(image);
        }

        return data;
    }
}


