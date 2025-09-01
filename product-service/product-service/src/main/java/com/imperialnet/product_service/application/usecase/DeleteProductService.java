// application/usecase/DeleteProductService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.port.in.DeleteProductUseCase;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public void deleteById(Long id) {
        log.info("Attempting to delete product with id={}", id);

        if (!productRepositoryPort.categoryExists(id)) {
            log.warn("Product with id={} not found", id);
            throw new IllegalArgumentException("Producto no encontrado con id " + id);
        }

        try {
            productRepositoryPort.deleteById(id);
            log.info("Successfully deleted product with id={}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete product with id={} because it is being used in other records", id, e);
            throw new IllegalStateException("No se puede eliminar el producto porque está asociado a otros registros.");
        }
    }
}
