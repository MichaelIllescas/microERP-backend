// application/dto/UpdateProductRequest.java
package com.imperialnet.product_service.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
}