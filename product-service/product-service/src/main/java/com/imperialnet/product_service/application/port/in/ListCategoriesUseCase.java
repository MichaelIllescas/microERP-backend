// application/port/in/ListCategoriesUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import java.util.List;

public interface ListCategoriesUseCase {
    List<CategoryResponse> getAllCategories();
}