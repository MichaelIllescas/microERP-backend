package com.imperialnet.product_service.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCategoryRequest {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    private String description;
}
