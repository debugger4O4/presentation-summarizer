package ru.debugger4o4.back.service.impl;

import chat.giga.client.GigaChatClient;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.http.client.HttpClientException;
import chat.giga.model.Scope;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.CompletionRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.dto.SlideData;
import ru.debugger4o4.back.service.CraftService;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import ru.debugger4o4.back.service.CraftService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

@Service
public class CraftServiceImpl implements CraftService {

    private static String task = """
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

    public static void main(String[] args) {
        long slidesCount = Arrays
                .stream(task.split("(\\*\\*Слайд )"))
                .filter(f -> !f.isEmpty())
                .count();

        // Предполагаемый URL изображения
        String imageUrl = "https://giga.chat/gigachat/files/public/generated/4398cbfe-d89d-48a2-9425-d9a9887eb2fb";

        downloadImageFromUrl();

    }

    private static void downloadImageFromUrl() {
        GigaChatClient client = GigaChatClient.builder()
                .authClient(AuthClient.builder()
                        .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                                .scope(Scope.GIGACHAT_API_CORP)
                                .authKey("OWU5ZmY0NzItNjQ5NS00Y2FmLTkzZjMtZjNmZmM4ZTA3ZGM2OjIyMjM3N2QxLWRiNzItNDY0OS04ZmU2LWFmNzU5NDEyNTNjZQ==")
                                .build())
                        .build())
                .build();
        try {
            // Получаем список моделей
            var modelResponse = client.models();
            if (modelResponse != null) {
                var completionsResponse = client.completions(CompletionRequest.builder()
                        .model(modelResponse.data().get(0).id())
                        .messages(List.of(
                                ChatMessage.builder()
                                        .role(ChatMessage.Role.SYSTEM)
                                        .content("Ты — художник Густав Климт")
                                        .build(),
                                ChatMessage.builder()
                                        .role(ChatMessage.Role.USER)
                                        .content("Нарисуй розового кота")
                                        .build()))
                        .build());
                String content = completionsResponse.choices().get(0).message().content();
                System.out.println(content);
            }
        } catch (HttpClientException ex) {
            System.out.println(ex.statusCode() + " " + ex.bodyAsString());
        }
    }

    @Override
    public void completeTechnicalTask(String technicalTask) {

    }

    private void createPresentation(String[] slides) {
        try (XMLSlideShow presentation = new XMLSlideShow();
             FileOutputStream out = new FileOutputStream(new File("presentation.pptx"))) {
            for (String slideContent : slides) {
                XSLFSlide slide = presentation.createSlide();
                XSLFTextBox shape = slide.createTextBox();
                XSLFTextParagraph p = shape.addNewTextParagraph();
                XSLFTextRun r = p.addNewTextRun();
                r.setText(slideContent);
                SlideData slideData = extractSlideData(slideContent);
                String imageDescription = slideData.getImage();
                if (imageDescription != null && !imageDescription.isEmpty()) {
                    String generatedImageUrl = generateImage(imageDescription);
                    // TODO: Добавить загрузку и размещение изображения
                }
                presentation.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SlideData extractSlideData(String slideContent) {
        SlideData data = new SlideData();
        Pattern titlePattern = Pattern.compile("\\*\\*Слайд \\d+: (.+)");
        Matcher matcher = titlePattern.matcher(slideContent);
        if (matcher.find()) {
            data.setTitle(matcher.group(1));
        }
        Pattern textPattern = Pattern.compile("\\*\\*Текст:\\s*([\\s\\S]*?)\\*\\*");
        matcher = textPattern.matcher(slideContent);
        if (matcher.find()) {
            data.setText(matcher.group(1));
        }
        Pattern imagePattern = Pattern.compile("(\\*\\*Картинка:\\s*([\\s\\S]*?)\\*\\*)|(\\*\\*Визуализация:\\s*([\\s\\S]*?)\\*\\*)");
        matcher = imagePattern.matcher(slideContent);
        if (matcher.find()) {
            data.setImage(matcher.group(1));
        }
        return data;
    }

    private String generateImage(String description) {
        // TODO: Реализовать запрос к API GigaChat
        return "URL_OF_GENERATED_IMAGE";
    }
}

