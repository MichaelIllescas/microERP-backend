// application/port/in/UpdateProductUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.UpdateProductRequest;

public interface UpdateProductUseCase {
    ProductResponse update(Long id, UpdateProductRequest request);
}
