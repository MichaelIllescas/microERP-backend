package com.imperialnet.customer_service.infrastructure.adapter.in;

import com.imperialnet.customer_service.application.dto.*;
import com.imperialnet.customer_service.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;

    // ================= CREATE =================
    @Operation(summary = "Crear un cliente", description = "Registra un nuevo cliente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = createCustomerUseCase.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= READ (ALL) =================
    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = getCustomerUseCase.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // ================= READ (BY ID) =================
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        CustomerResponse response = getCustomerUseCase.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    // ================= UPDATE =================
    @Operation(summary = "Actualizar cliente", description = "Modifica los datos de un cliente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerResponse response = updateCustomerUseCase.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    // ================= DELETE =================
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        deleteCustomerUseCase.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
