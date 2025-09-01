// application/port/in/ListProductsUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.ProductResponse;
import java.util.List;

public interface ListProductsUseCase {
    List<ProductResponse> getAllProducts();
}