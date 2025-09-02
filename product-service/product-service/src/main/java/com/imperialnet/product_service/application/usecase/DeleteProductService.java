package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductDeletedEvent;
import com.imperialnet.product_service.application.event.ProductEventPublisher;
import com.imperialnet.product_service.application.port.in.DeleteProductUseCase;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductEventPublisher eventPublisher; // 👈 nuevo

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Attempting to delete product with id={}", id);

        if (productRepositoryPort.findById(id).isEmpty()) {
            log.warn("Product with id={} not found", id);
            throw new IllegalArgumentException("Producto no encontrado con id " + id);
        }

        try {
            productRepositoryPort.deleteById(id);
            log.info("Successfully deleted product with id={}", id);

            // 📢 Publicar evento de eliminación
            ProductDeletedEvent event = ProductDeletedEvent.builder()
                    .productId(id)
                    .build();

            eventPublisher.publishProductDeleted(event);

        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete product with id={} because it is being used in other records", id, e);
            throw new IllegalStateException("No se puede eliminar el producto porque está asociado a otros registros.");
        }
    }
}
