// application/dto/UpdateCategoryRequest.java
package com.imperialnet.product_service.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {
    private String name;
    private String description;
}