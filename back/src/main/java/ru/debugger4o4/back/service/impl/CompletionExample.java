package ru.debugger4o4.back.service.impl;

import chat.giga.client.GigaChatClient;
import chat.giga.client.auth.AuthClient;
import chat.giga.client.auth.AuthClientBuilder;
import chat.giga.http.client.HttpClientException;
import chat.giga.model.ModelName;
import chat.giga.model.Scope;
import chat.giga.model.completion.ChatMessage;
import chat.giga.model.completion.CompletionRequest;
import chat.giga.model.completion.CompletionResponse;
import org.springframework.beans.factory.annotation.Value;

public class CompletionExample {

    @Value("${gigachat.openapi.key}")
    private static String openaiApiKey;
    @Value("${gigachat.client.id}")
    private static String clientId;


    public static void main(String[] args) {
        GigaChatClient client = GigaChatClient.builder()
                .authClient(AuthClient.builder()
                        .withOAuth(AuthClientBuilder.OAuthBuilder.builder()
                                .authKey(openaiApiKey)
                                .build())
                        .build())
                .build();

        CompletionRequest.CompletionRequestBuilder builder = CompletionRequest.builder()
                .model(ModelName.GIGA_CHAT_PRO)
                .message(ChatMessage.builder()
                        .content("Когда уже ИИ захватит этот мир?")
                        .role(ChatMessage.Role.USER)
                        .build())
                .message(ChatMessage.builder()
                        .content("Пока что это не является неизбежным событием. Несмотря на то, что искусственный интеллект (ИИ) развивается быстрыми темпами и может выполнять сложные задачи все более эффективно, он по-прежнему ограничен в своих возможностях и не может заменить полностью человека во многих областях. Кроме того, существуют этические и правовые вопросы, связанные с использованием ИИ, которые необходимо учитывать при его разработке и внедрении.")
                        .role(ChatMessage.Role.ASSISTANT).build());

        try {
            for (int i = 0; i < 4; i++) {
                CompletionRequest request = builder.build();
                CompletionResponse response = client.completions(request);
                System.out.println(response);

                response.choices().forEach(e -> builder.message(e.message().ofAssistantMessage()));

                builder.message(ChatMessage.builder()
                        .content("Думаешь, у нас еще есть шанс?")
                        .role(ChatMessage.Role.USER).build());
            }
        } catch (HttpClientException ex) {
            System.out.println(ex.statusCode() + " " + ex.bodyAsString());
        }
    }
}
