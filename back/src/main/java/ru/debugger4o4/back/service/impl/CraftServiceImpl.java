package ru.debugger4o4.back.service.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.SlideData;
import ru.debugger4o4.back.service.CraftService;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.util.Util;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CraftServiceImpl implements CraftService {

    private final GigaChatService gigaChatService;

    private static final Logger logger = LoggerFactory.getLogger(Util.class);


    public CraftServiceImpl(GigaChatService gigaChatService) {
        this.gigaChatService = gigaChatService;
    }

    @Override
    public void completeTechnicalTask(String technicalTask, ByteArrayOutputStream outputStream) {
        technicalTask = """
            **Слайд 1: Введение**

            **Название:** Реконкиста \s
            **Подзаголовок:** Длительный процесс отвоевания Пиренейского полуострова христианами \s

            **Текст:** \s
            Реконкиста — длительный процесс, длившийся почти восемь веков (718–1492 гг.), в ходе которого христианские королевства Пиренейского полуострова отвоевывали земли, захваченные мусульманами (маврами). \s

            **Картинка:** \s
            Карта Пиренейского полуострова VIII века с указанием территорий христианских и мусульманских государств.

            ---

            **Слайд 2: Причины Реконкисты**

            **Название:** Причины Реконкисты \s

            **Текст:** \s
            - **Религиозный конфликт:** Стремление христианских правителей восстановить господство на захваченных маврами территориях. \s
            - **Политическая раздробленность:** Многочисленные мелкие христианские и мусульманские княжества, стремящиеся расширить свои владения. \s
            - **Экономические мотивы:** Захват новых земель позволял увеличивать ресурсы и богатство, укрепляя военную и политическую мощь. \s

            **График:** \s
            Диаграмма, показывающая изменение территорий христианских и мусульманских государств на Пиренейском полуострове в период Реконкисты.

            ---

            **Слайд 3: Этапы Реконкисты**

            **Название:** Этапы Реконкисты \s

            **Текст:** \s
            - **VIII–XI века:** Формирование первых христианских государств (Астурия, Леон, Наварра, Кастилия, Арагон). \s
            - **X–XIII века:** Переломный момент: распад Кордовского халифата (1031 г.), захват Толедо (1085 г.). \s
            - **XIII–XV века:** Завоевание крупнейших мусульманских территорий (Кордова, Севилья, Гранада). \s

            **Картинка:** \s
            Изображение сражения христианских войск с маврами.

            ---

            **Слайд 4: Итоги Реконкисты**

            **Название:** Итоги Реконкисты \s

            **Текст:** \s
            - Образование централизованных государств (Португалия, Кастилия, Арагон, Наварра). \s
            - Объединение кастильских и арагонских земель в Испанское королевство благодаря династическому браку Фердинанда II и Изабеллы I. \s
            - Изгнание с полуострова мусульман и евреев, христианизация оставшихся. \s


            """;
        String[] slides = Arrays
                .stream(technicalTask.split("(\\*\\*Слайд )"))
                .filter(f -> !f.isEmpty())
                .map(String::trim)
                .toArray(String[]::new);
        createPresentation(slides, outputStream);
    }

    private void createPresentation(String[] slides, ByteArrayOutputStream outputStream) {
        try (XMLSlideShow presentation = new XMLSlideShow()) {
            for (String slideContent : slides) {
                XSLFSlide slide = presentation.createSlide();
                XSLFTextBox shape = slide.createTextBox();
                shape.setAnchor(new Rectangle(100, 100, 600, 300));
                XSLFTextParagraph p = shape.addNewTextParagraph();
                XSLFTextRun r = p.addNewTextRun();
                SlideData slideData = extractSlideData(slideContent);
                r.setText(slideData.getText());
                String imageDescription = slideData.getImage();
                if (imageDescription != null && !imageDescription.isEmpty()) {
                    gigaChatService.sendQueryToGenerateAndDownloadImage(slide, imageDescription);
                }
                presentation.write(outputStream);
            }
        } catch (Exception e) {
            logger.error("CraftServiceImpl exception on createPresentation(): {}", e.getMessage());
        }
    }

    private SlideData extractSlideData(String slideContent) {
        SlideData data = new SlideData();

        Pattern titlePattern = Pattern.compile("Название:([\\s\\S]*?)Подзаголовок:([\\s\\S]*?)Текст:");
        Matcher matcher = titlePattern.matcher(slideContent);
        if (matcher.find()) {
            data.setTitle(matcher.group(1).trim());
            data.setSubtitle(matcher.group(2).trim());
        }

        Pattern textPattern = Pattern.compile("Текст:([\\s\\S]*?)Картинка:");
        matcher = textPattern.matcher(slideContent);
        if (matcher.find()) {
            data.setText(matcher.group(1).trim());
        }

        Pattern imagePattern = Pattern.compile("Картинка:([\\s\\S]*)");
        matcher = imagePattern.matcher(slideContent);
        if (matcher.find()) {
            data.setImage(matcher.group(1).trim());
        }

        return data;
    }


}

