package com.imperialnet.sales_service.infrastructure.adapter.in;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleResponse;
import com.imperialnet.sales_service.application.port.in.*;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventas", description = "Gestión de ventas en el sistema")
public class SaleController {

    private final CreateSaleUseCase registerSaleService;
    private final ListSalesUseCase listSalesUseCase;
    private final GetSaleByIdUseCase getSaleByIdUseCase;
    private final CancelSaleUseCase cancelSaleUseCase;
    private final ListSalesByCustomerUseCase listSalesByCustomerUseCase;
    private final ListSalesByDateRangeUseCase listSalesByDateRangeUseCase;



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

    @Operation(
            summary = "Listar ventas",
            description = "Obtiene todas las ventas registradas. " +
                    "Si no se envía el parámetro `status`, devuelve solo las confirmadas. " +
                    "Valores permitidos: `CONFIRMED`, `CANCELED`."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<SaleResponse>> listSales(
            @Parameter(description = "Estado de las ventas a filtrar. Ej: CONFIRMED, CANCELED")
            @RequestParam(value = "status", required = false) String status
    ) {
        log.info("📥 Petición recibida para listar ventas con estado: {}", status != null ? status : "DEFAULT (CONFIRMED)");

        List<SaleResponse> sales = listSalesUseCase.listSales(status);

        log.info("📤 Respuesta enviada con {} ventas encontradas", sales.size());

        return ResponseEntity.ok(sales);
    }

    @Operation(
            summary = "Obtener venta por ID",
            description = "Devuelve el detalle de una venta dado su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(
            @Parameter(description = "ID de la venta a buscar", example = "1")
            @PathVariable Long id
    ) {
        log.info("📥 Petición recibida para obtener venta con ID {}", id);
        SaleResponse response = getSaleByIdUseCase.getSaleById(id);
        log.info("📤 Respuesta enviada con venta ID {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancelar venta por ID",
            description = "Cancela una venta existente. Si ya está cancelada, devuelve un error claro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta cancelada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "400", description = "La venta ya estaba cancelada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<SaleResponse> cancelSale(
            @Parameter(description = "ID de la venta a cancelar", example = "1")
            @PathVariable Long id
    ) {
        log.info("📥 Petición recibida para cancelar venta con ID {}", id);
        SaleResponse response = cancelSaleUseCase.cancelSale(id);
        log.info("📤 Respuesta enviada: venta {} cancelada", id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/by-customer/{customerId}")
    @Operation(
            summary = "Listar ventas por cliente",
            description = "Obtiene todas las ventas asociadas a un cliente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas obtenidas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas para este cliente")
    })
    public ResponseEntity<List<SaleResponse>> listSalesByCustomer(
            @Parameter(description = "ID del cliente") @PathVariable Long customerId
    ) {
        log.info("➡️ GET /sales/by-customer/{}", customerId);
        List<SaleResponse> sales = listSalesByCustomerUseCase.listSalesByCustomer(customerId);
        return ResponseEntity.ok(sales);
    }

    @Operation(
            summary = "Listar ventas por rango de fechas",
            description = "Devuelve todas las ventas registradas entre dos fechas inclusive"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SaleResponse.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas en el rango",
                    content = @Content)
    })
    @GetMapping("/by-date-range")
    public ResponseEntity<List<SaleResponse>> listSalesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate.trim());
        LocalDate end = LocalDate.parse(endDate.trim());

        log.info("➡️ GET /sales/by-date-range?startDate={}&endDate={}", start, end);
        var response = listSalesByDateRangeUseCase.listSalesByDateRange(start, end);
        log.info("⬅️ {} ventas devueltas", response.size());
        return ResponseEntity.ok(response);
    }



}
