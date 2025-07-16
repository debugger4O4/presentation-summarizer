package ru.debugger4o4.back.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import ru.debugger4o4.back.service.TokenService;
import ru.debugger4o4.back.util.Util;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TokenServiceImpl implements TokenService {

    @Value("${gigachat.rq.uid}")
    private String rqUID;
    @Value("${gigachat.openapi.key}")
    private String openApiKey;
    @Value("${gigachat.get.token.url}")
    private String getTokenUrl;

    private Util util;

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final AtomicReference<String> currentToken = new AtomicReference<>("");

    @Scheduled(fixedRate = 29 * 60 * 1000)
    public void updateToken() {
        try {
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
            ResponseEntity<String> response = restTemplate.exchange(
                    getTokenUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            String newToken = util.getAccessToken(response.getBody());
            currentToken.set(newToken);
            logger.info("Token updated");
        } catch (Exception e) {
            logger.error("Token updated error: {}", e.getMessage());
        }
    }

    @Autowired
    public void setUtil(Util util) {
        this.util = util;
    }

    public TokenServiceImpl() {
        updateToken();
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

    @Override
    public String getCurrentToken() {
        return currentToken.get();
    }
}

