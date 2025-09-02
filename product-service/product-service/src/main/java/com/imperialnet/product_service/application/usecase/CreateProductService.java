package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.ProductCreatedEvent;
import com.imperialnet.product_service.application.event.ProductEventPublisher;
import com.imperialnet.product_service.application.port.in.CreateProductUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort repositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final ProductMapper mapper;
    private final ProductEventPublisher eventPublisher;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("▶️ Iniciando creación de producto: {}", request);

        if (!categoryRepositoryPort.existsById(request.getCategoryId())) {
            log.error("❌ Error: la categoría con ID {} no existe", request.getCategoryId());
            throw new IllegalArgumentException("La categoría no existe");
        }

        Product product = mapper.toDomain(request);
        log.debug("📦 Producto mapeado a dominio: {}", product);

        Product saved = repositoryPort.save(product);
        log.info("💾 Producto guardado con ID: {}", saved.getId());

        // 📢 Publicar evento
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(saved.getId())
                .name(saved.getName())
                .categoryId(saved.getCategory().getId())
                .initialQuantity(request.getInitialQuantity())
                .build();

        eventPublisher.publishProductCreated(event);

        ProductResponse response = mapper.toResponse(saved);
        log.debug("📤 Respuesta generada: {}", response);

        return response;
    }
}
