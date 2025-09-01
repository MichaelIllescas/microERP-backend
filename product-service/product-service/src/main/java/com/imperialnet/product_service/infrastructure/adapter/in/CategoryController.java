package com.imperialnet.product_service.infrastructure.adapter.in;

import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.application.dto.CreateCategoryRequest;
import com.imperialnet.product_service.application.dto.UpdateCategoryRequest;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías")
@Slf4j
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("📥 Solicitud para crear categoría: {}", request);
        CategoryResponse response = createCategoryUseCase.createCategory(request);
        log.info("✅ Categoría creada con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías")
    @ApiResponse(responseCode = "200", description = "Listado de Categorias obtenidas exitosamente")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
         log.info("📥 Solicitando lista de categorías");
        log.info("✅ Lista de categorías obtenida, total: {}", listCategoriesUseCase.getAllCategories().size());
        return ResponseEntity.ok(listCategoriesUseCase.getAllCategories());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Received request to delete category with id={}", id);
        deleteCategoryUseCase.deleteById(id);
        log.info("Category with id={} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("Received request to get category with id={}", id);
        return ResponseEntity.ok(getCategoryByIdUseCase.getById(id));
    }

    // infrastructure/adapter/in/CategoryController.java
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría por ID")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {
        log.info("Received request to update category with id={}", id);
        return ResponseEntity.ok(updateCategoryUseCase.update(id, request));
    }
}
