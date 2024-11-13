package com.cookease.cook_ease.controller;

import com.cookease.cook_ease.dto.ChatRequest;
import com.cookease.cook_ease.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@Validated
public class ChatGPTController {

    private static final Logger logger = LoggerFactory.getLogger(ChatGPTController.class);

    @Autowired
    private OpenAIService openAIService;

    /**
     * Endpoint para procesar una solicitud de chat.
     *
     * @param chatRequest Objeto que contiene los ingredientes.
     * @return Respuesta generada por el servicio de OpenAI.
     */
    @PostMapping
    public ResponseEntity<?> chat(@RequestBody @Validated ChatRequest chatRequest) {
        logger.info("Recibiendo solicitud con ingredientes: {}", chatRequest.getIngrediente());
        try {
            String receta = openAIService.generateRecipe(chatRequest.getIngrediente());
            logger.debug("Receta generada: {}", receta);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            logger.error("Error al generar la receta: {}", e.getMessage());
            return ResponseEntity.status(500).body("Ocurrió un error al procesar la solicitud: " + e.getMessage());
        }
    }
}
