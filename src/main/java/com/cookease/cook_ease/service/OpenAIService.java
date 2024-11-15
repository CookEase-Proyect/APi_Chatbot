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

    @Value("${openai-service.gpt-model}")  // Corregido "openai-servie" a "openai-service"
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
        headers.set("Authorization", "Bearer " + openAiApiKey);  // Configurar encabezado correctamente

        // Construir el cuerpo de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("model", gptModel);
        body.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", "Eres un asistente que ayuda a crear recetas saludables.");
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", "Dame el Nombre, Descripción (breve) e Instrucciones (en pasos numerados) de una receta saludable con: " + ingredientes + ". Responde en un formato compacto.");
                }}
        });

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Realizar la solicitud POST
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        // Manejar la respuesta y extraer el contenido de la receta
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
