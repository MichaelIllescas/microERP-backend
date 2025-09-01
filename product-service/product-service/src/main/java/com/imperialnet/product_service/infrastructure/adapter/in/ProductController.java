package com.imperialnet.product_service.infrastructure.adapter.in;

import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.UpdateProductRequest;
import com.imperialnet.product_service.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
@Slf4j
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final UpdateProductUseCase updateProductUseCase;


    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        log.info("📥 Recibida solicitud para crear producto: {}", request);

        ProductResponse response = createProductUseCase.createProduct(request);

        log.info("✅ Producto creado con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    @ApiResponse(responseCode = "200", description = "Listado de productos obtenido exitosamente")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("✅ Solicitando lista de productos");
        log.info("✅ Se obtuvieron {} productos", listProductsUseCase.getAllProducts().size());
        return ResponseEntity.ok(listProductsUseCase.getAllProducts());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with id={}", id);
        deleteProductUseCase.deleteById(id);
        log.info("Product with id={} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("Received request to get product with id={}", id);
        return ResponseEntity.ok(getProductByIdUseCase.getById(id));
    }

    // infrastructure/adapter/in/ProductController.java
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto por ID")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request) {
        log.info("Received request to update product with id={}", id);
        return ResponseEntity.ok(updateProductUseCase.update(id, request));
    }
}
