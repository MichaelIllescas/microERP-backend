package com.imperialnet.stock_service.infraestructure.adapter.in;

import com.imperialnet.stock_service.application.dto.UpdateStockRequest;
import com.imperialnet.stock_service.application.dto.StockResponse;
import com.imperialnet.stock_service.application.port.in.GetStockByProductIdUseCase;
import com.imperialnet.stock_service.application.port.in.GetAllStockUseCase;
import com.imperialnet.stock_service.application.port.in.UpdateStockByProductIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@Tag(name = "Stock", description = "Gestión de inventario")
public class StockController {

    private final GetStockByProductIdUseCase getStockByProductIdUseCase;
    private final GetAllStockUseCase listAllStockUseCase;
    private final UpdateStockByProductIdUseCase updateStockByProductIdUseCase;

    @Operation(summary = "Obtener el stock de un producto por su productId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en stock")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> getStockByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(getStockByProductIdUseCase.getStockByProductId(productId));
    }

    @Operation(summary = "Listar todos los stocks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de stock")
    })
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStock() {
        return ResponseEntity.ok(listAllStockUseCase.getAllStock());
    }

    @Operation(summary = "Actualizar el stock de un producto por su productId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en stock")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<StockResponse> updateStockByProductId(
            @PathVariable Long productId,
            @RequestBody UpdateStockRequest request
    ) {
        StockResponse response = updateStockByProductIdUseCase.updateStockByProductId(productId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
