// application/port/in/GetProductByIdUseCase.java
package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.ProductResponse;

public interface GetProductByIdUseCase {
    ProductResponse getById(Long id);
}
