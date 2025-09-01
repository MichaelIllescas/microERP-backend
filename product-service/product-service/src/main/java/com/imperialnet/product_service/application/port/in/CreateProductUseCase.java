package com.imperialnet.product_service.application.port.in;

import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;

public interface CreateProductUseCase {
    ProductResponse createProduct(CreateProductRequest request);
}