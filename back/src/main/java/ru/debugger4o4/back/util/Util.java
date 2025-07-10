package ru.debugger4o4.back.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.debugger4o4.back.dto.Promt;
import ru.debugger4o4.back.dto.ResponseBodyData;

@Component
public class Util {

    @Value("${keystore.path}")
    private String keystorePath;
    @Value("${keystore.pass}")
    private String keystorePass;

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public void setCertificates() {
        try {
            KeyStore keyStore = loadKeyStore(keystorePath, keystorePass);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static KeyStore loadKeyStore(String keystorePath, String keystorePass) {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                ks.load(fis, keystorePass.toCharArray());
            }
        } catch (Exception e) {
            logger.error("Util exception in loadKeyStore: {}", e.getMessage());
        }
        return ks;
    }

    public String getAccessToken(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseBodyData responseBodyData = mapper.readValue(responseBody, ResponseBodyData.class);
            return responseBodyData.getAccess_token();
        } catch (JsonProcessingException e) {
            logger.error("Util exception in getAccessToken: {}", e.getMessage());
        }
        return null;
    }

    public String getContent(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Promt promt = mapper.readValue(responseBody, Promt.class);
            if (promt.getChoices() != null && promt.getChoices().size() > 0) {
                return promt.getChoices().get(0).getMessage().getContent();
            }
        } catch (JsonProcessingException e) {
            logger.error("Util exception in getContent: {}", e.getMessage());
        }
        return null;
    }
}
