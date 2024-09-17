package com.cookease.cook_ease.dto;


import jakarta.validation.constraints.NotEmpty;

public class ChatRequest {

    @NotEmpty(message = "El campo 'ingrediente' no puede estar vacío.")
    private String ingrediente;

    // Getters y Setters

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public String toString() {
        return "ingrediente=" + ingrediente;
    }
}
