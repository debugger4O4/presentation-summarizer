package ru.debugger4o4.back.service.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.debugger4o4.back.dto.RequestSummarizeData;
import ru.debugger4o4.back.service.GigaChatService;
import ru.debugger4o4.back.util.Util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import static ru.debugger4o4.back.dictionary.GigaChatModels.GigaChat2Max;


@Service
public class GigaChatServiceImpl implements GigaChatService {

    @Value("${gigachat.get.model.url}")
    private String getModelsUrl;
    @Value("${gigachat.get.token.url}")
    private String getTokenUrl;
    @Value("${gigachat.send.query.url}")
    private String sendQueryUrl;
    @Value("${gigachat.rq.uid}")
    private String rqUID;
    @Value("${gigachat.openapi.key}")
    private String openApiKey;

    private final Util util;
    private static Util util1;

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public GigaChatServiceImpl(Util util) {
        this.util = util;
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
                         Количество слайдов = %d. Текст для суммаризации: \
                         %s" \
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
        headers.setBearerAuth(getToken());
        HttpEntity<?> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(sendQueryUrl, HttpMethod.POST, entity, String.class);
            return util.getContent(response.getBody());
        } catch (Exception e) {
            // Если произошел сбой, попытка обновить токен и выполнить запрос повторно.
            logger.info("GigaChatServiceImpl sendQuery error or reissue of the token: {}", e.getMessage());
            headers.setBearerAuth(getToken());
            ResponseEntity<String> response = restTemplate.exchange(sendQueryUrl, HttpMethod.POST, entity, String.class);
            return util.getContent(response.getBody());
        }
    }

//    private void sendQueryForDownloadImage(String imageUrl) {
//        util1.setCertificates();
//        String payload = """
//                {
//                  "model": "%s",
//                  "messages": [
//                    {
//                      "role": "user",
//                      "content": ### Визуализация
//                                         - **График**: Количество христианских и мусульманских территорий на Пиренейском полуострове в разные периоды Реконкисты.
//                                         - **Картинка**: Карта Пиренейского полуострова с обозначением территорий, захваченных христианами в разные века.
//                                         - **Статистика**: Доля христианских и мусульманских территорий в начале и конце Реконкисты.
//                    }
//                  ],
//                  "n": 1,
//                  "stream": false,
//                  "max_tokens": 512,
//                  "repetition_penalty": 1,
//                  "update_interval": 0
//                }
//                """.formatted(GigaChat2Max.getValue());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(getToken());
//        HttpEntity<?> entity = new HttpEntity<>(payload, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
//            HttpGet request = new HttpGet(imageUrl);
//
//            try (CloseableHttpResponse response = httpclient.execute(request)) {
//                if (response.getStatusLine().getStatusCode() != 200) {
//                    throw new RuntimeException("Ошибка HTTP: " + response.getStatusLine());
//                }
//
//                org.apache.http.HttpEntity entity = response.getEntity();
//                byte[] bytes = EntityUtils.toByteArray(entity); // Получаем байтовый массив изображения
//
//                Files.write(Paths.get("downloaded_image.jpg"), bytes); // Сохраняем файл
//            }
//        }
//        try {
//            org.apache.http.HttpEntity entity = response.getEntity();
//            byte[] bytes = EntityUtils.toByteArray(entity); // Получаем байтовый массив изображения
//
//            Files.write(Paths.get("downloaded_image.jpg"), bytes);
//        } catch (Exception e) {
//            // Если произошел сбой, попытка обновить токен и выполнить запрос повторно.
//            logger.info("GigaChatServiceImpl sendQuery error or reissue of the token: {}", e.getMessage());
//            headers.setBearerAuth(getToken());
//            ResponseEntity<String> response = restTemplate.exchange(sendQueryUrl, HttpMethod.POST, entity, String.class);
//            return util.getContent(response.getBody());
//        }
//    }
//
//    public static void main(String[] args) {
//        sendQueryForDownloadImage(imageUrl);
//    }

    @Override
    public void getModels() {
        util.setCertificates();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(getToken());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(getModelsUrl, HttpMethod.GET, entity, String.class);
            logger.info("GigaChatServiceImpl getModels: {}", response.getBody());
        } catch (Exception e) {
            // Если произошел сбой, попытка обновить токен и выполнить запрос повторно.
            headers.setBearerAuth(getToken());
            ResponseEntity<String> response = restTemplate.exchange(getModelsUrl, HttpMethod.GET, entity, String.class);
            logger.info("GigaChatServiceImpl getModels after retry: {}", response.getBody());
        }
    }

    @Override
    public String getToken() {
        util.setCertificates();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("RqUID", rqUID);
        headers.setBasicAuth(openApiKey);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("scope", "GIGACHAT_API_PERS");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(getTokenUrl, HttpMethod.POST, entity, String.class);
        return util.getAccessToken(response.getBody());
    }
}
