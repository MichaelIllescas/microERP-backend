package com.imperialnet.product_service.domain.model;

import lombok.*;

/**
 * Modelo de dominio para Producto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;

    // Relación con categoría (solo referencia)
    private Category category;
}
