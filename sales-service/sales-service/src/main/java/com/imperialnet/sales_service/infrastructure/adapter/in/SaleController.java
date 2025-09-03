package com.imperialnet.sales_service.infrastructure.adapter.in;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.CreateSaleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventas", description = "Gestión de ventas en el sistema")
public class SaleController {

    private final CreateSaleUseCase registerSaleService;

    /**
     * Endpoint para registrar una nueva venta.
     */
    @Operation(
            summary = "Registrar una venta",
            description = "Crea una nueva venta validando cliente, productos y stock disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente",
                    content = @Content(schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cliente inexistente o stock insuficiente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, token inválido o ausente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error inesperado en el sistema", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody CreateSaleRequest request) {
        log.info("➡️ [POST /sales] Nueva solicitud de registro de venta para cliente {}", request.getCustomerId());
        log.debug("📥 Payload recibido: {}", request);

        SaleResponse response = registerSaleService.registerSale(request);

        log.info("✅ Venta registrada correctamente con ID {}", response.getSaleId());
        log.debug("📤 Respuesta enviada: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
