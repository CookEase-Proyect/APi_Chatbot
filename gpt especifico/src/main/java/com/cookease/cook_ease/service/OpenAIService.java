package com.cookease.cook_ease.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai-service.api-key}")
    private String openAiApiKey;

    @Value("${openai-service.urls.base-url}")
    private String baseUrl;

    @Value("${openai-service.urls.chat-url}")
    private String chatUrl;

    @Value("${openai-servie.gpt-model}")
    private String gptModel;

    @Value("${openai-service.http-client.read.timeout}")
    private int readTimeout;

    @Value("${openai-service.http-client.connect.timeout}")
    private int connectTimeout;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateRecipe(String ingredientes) throws Exception {
        String apiUrl = baseUrl + chatUrl;

        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        // Construir el cuerpo de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("model", gptModel); // Por ejemplo, "gpt-3.5-turbo"
        body.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", "Eres un asistente que ayuda a crear recetas saludables.");
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", "Dame por separado el Nombre, Descripción y solo Instrucciones resumido en un párrafo de una receta saludable con: " + ingredientes);
                }}
        });

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Realizar la solicitud POST
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                Object choicesObj = responseBody.get("choices");
                if (choicesObj instanceof Iterable) {
                    for (Object choice : (Iterable) choicesObj) {
                        if (choice instanceof Map) {
                            Map choiceMap = (Map) choice;
                            if (choiceMap.containsKey("message")) {
                                Map messageMap = (Map) choiceMap.get("message");
                                if (messageMap.containsKey("content")) {
                                    return (String) messageMap.get("content");
                                }
                            }
                        }
                    }
                }
            }
        }

        throw new Exception("No se pudo generar la receta.");
    }
}
