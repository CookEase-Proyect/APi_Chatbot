package com.cookease.cook_ease.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comida")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 2000)
    private String instrucciones;

    // Otros campos como ingredientes, imagen, etc., pueden ser añadidos aquí
}
