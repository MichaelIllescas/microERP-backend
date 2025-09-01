// application/port/in/GetCategoryByIdUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.CategoryResponse;

public interface GetCategoryByIdUseCase {
    CategoryResponse getById(Long id);
}
