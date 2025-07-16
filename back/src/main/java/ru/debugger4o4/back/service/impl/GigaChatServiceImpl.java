package ru.debugger4o4.back.service.impl;

import chat.giga.client.GigaChatClient;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.http.client.HttpClientException;
import chat.giga.model.ModelResponse;
import chat.giga.model.Scope;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.CompletionRequest;
import chat.giga.model.completion.CompletionResponse;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.debugger4o4.back.dto.RequestSummarizeData;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.service.TokenService;
import ru.debugger4o4.back.util.Util;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static ru.debugger4o4.back.dictionary.GigaChatModels.GigaChat2Max;


@Service
public class GigaChatServiceImpl implements GigaChatService {

    @Value("${gigachat.get.model.url}")
    private String getModelsUrl;
    @Value("${gigachat.send.query.url}")
    private String sendQueryUrl;
    @Value("${gigachat.get.file.url}")
    private String getFileUrl;
    @Value("${gigachat.openapi.key}")
    private String openApiKey;

    private Util util;

    private TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    @Autowired
    public void setUtil(Util util) {
        this.util = util;
    }

    @Autowired
    public void setTokenUpdater(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public String sendQueryForTechnicalTask(RequestSummarizeData requestSummarizeData) {
        util.setCertificates();
        int slidesCount = requestSummarizeData.getSlidesCount();
        String textForSummarize = requestSummarizeData.getTextForSummarize().replaceAll("[^а-яА-Яa-zA-Z0-9 ]", "");
        String payload = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "Суммаризируй текст и сделай презентацию с текстом, картинками, графиками и статистикой. \
                                 Количество слайдов = %d. Каждый слайд дожен иметь флаг-слово **Слайд <номер>**, каждый заголовок дожен \
                                 иметь флаг-слово **Заголовок**, каждая картинка должна иметь флаг-слово **Картинка**, \
                                 каждый график должен иметь флаг-слово **График**, каждая статистика должна имет флаг-слово \
                                 **Статистика**. Не вставляй символ * внутри раздела **Текст**, пример как делать не надо: \
                                 **VIII–XI века:**. Текст для суммаризации: \
                                 %s"
                    }
                  ],
                  "n": 1,
                  "stream": false,
                  "max_tokens": 512,
                  "repetition_penalty": 1,
                  "update_interval": 0
                }
                """.formatted(GigaChat2Max.getValue(), slidesCount, textForSummarize);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenService.getCurrentToken());
        HttpEntity<?> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(sendQueryUrl, HttpMethod.POST, entity, String.class);
        return util.getContent(response.getBody());
    }

    @Override
    public void sendQueryToGenerateAndDownloadImage(XMLSlideShow presentation, XSLFSlide slide, String pictureDescription) {
        GigaChatClient client = GigaChatClient.builder()
                .authClient(AuthClient.builder()
                        .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                                .scope(Scope.GIGACHAT_API_PERS)
                                .authKey(openApiKey)
                                .build())
                        .build())
                .build();
        try {
            // Получаем список моделей.
            ModelResponse modelResponse = client.models();
            if (modelResponse != null) {
                CompletionResponse completionsResponse = client.completions(CompletionRequest.builder()
                        .model(modelResponse.data().get(0).id())
                        .messages(List.of(
                                ChatMessage.builder()
                                        .role(ChatMessage.Role.SYSTEM)
                                        .build(),
                                ChatMessage.builder()
                                        .role(ChatMessage.Role.USER)
                                        .content("Нарисуй изображение: " + pictureDescription)
                                        .build()))
                        .build());
                String content = completionsResponse.choices().get(0).message().content();
                Pattern pattern = Pattern.compile("src=\"([^\"]*)\"");
                Matcher matcher = pattern.matcher(content);
                String src = "";
                if (matcher.find()) {
                    src = matcher.group(1);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    headers.setBearerAuth(tokenService.getCurrentToken());
                    HttpEntity<?> entity = new HttpEntity<>(null, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<byte[]> response = restTemplate.exchange(getFileUrl + "/" + src + "/content", HttpMethod.GET, entity, byte[].class);

//                        // Для теста.
//                        try (FileOutputStream fos = new FileOutputStream("image.jpg")) {
//                            fos.write(response.getBody());
//                            fos.flush();
//                        }

                    XSLFPictureData pd = presentation.addPicture(response.getBody(), PictureData.PictureType.PNG);
                    XSLFPictureShape picture = slide.createPicture(pd);
                    picture.setAnchor(new Rectangle(200, 300, 300, 200));
                }
            }
        } catch (HttpClientException ex) {
            logger.error("GigaChatServiceImpl sendQueryToGenerateAndDownloadImage exception: {}", ex.getMessage());
        }

    }

    @Override
    public void getModels() {
        util.setCertificates();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenService.getCurrentToken());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(getModelsUrl, HttpMethod.GET, entity, String.class);
        logger.info("GigaChatServiceImpl getModels: {}", response.getBody());
    }
}
