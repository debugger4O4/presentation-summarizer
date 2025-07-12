package ru.debugger4o4.back.dictionary;

public enum GigaChatModels {

    GigaChat ("GigaChat"),
    GigaChat2 ("GigaChat-2"),
    GigaChat2Max ("GigaChat-2-Max"),
    GigaChat2Pro ("GigaChat-2-Pro"),
    GigaChatMax("GigaChat-Max"),
    GigaChatMaxPreview("GigaChat-Max-preview"),
    GigaChatPlus("GigaChat-Plus"),
    GigaChatPlusPreview("GigaChat-Plus-preview"),
    GigaChatPro("GigaChat-Pro"),
    GigaChatProPreview("GigaChat-Pro-preview"),
    GigaChatPreview("GigaChat-preview"),
    Embeddings("Embeddings"),
    Embeddings2("Embeddings-2"),
    EmbeddingsGigaR("EmbeddingsGigaR");

    private final String value;

    GigaChatModels(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GigaChatModels{" +
                "value='" + value + '\'' +
                '}';
    }
}
