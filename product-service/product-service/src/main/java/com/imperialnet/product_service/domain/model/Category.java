package com.imperialnet.product_service.domain.model;

import lombok.*;

/**
 * Modelo de dominio para Categoría.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Category {
    private Long id;
    private String name;
    private String description;
}
