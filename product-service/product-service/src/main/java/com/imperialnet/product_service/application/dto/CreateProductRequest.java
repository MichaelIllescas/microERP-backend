package com.imperialnet.product_service.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;

    // Campo opcional → puede venir null
    private Integer initialQuantity;
}

