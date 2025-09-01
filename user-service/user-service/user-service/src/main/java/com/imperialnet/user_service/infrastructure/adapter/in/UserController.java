package com.imperialnet.user_service.infrastructure.adapter.in;

import com.imperialnet.user_service.application.dto.*;
import com.imperialnet.user_service.application.port.in.*;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;


    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en Keycloak y en la BD local")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve la lista completa de usuarios registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(listUsersUseCase.listAll());
    }


    // 🔹 GET /users/{id} → Obtiene uno por id
    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve la información de un usuario específico dado su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID del usuario a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(getUserByIdUseCase.getById(id));
    }

    @Operation(
            summary = "Actualizar usuario por ID",
            description = "Actualiza parcialmente los datos del usuario. Los campos nulos no se modifican.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Prohibido"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "409", description = "Email en uso")
            }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody UpdateUserRequest request) {
        return updateUserUseCase.execute(id, request);
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina el usuario de la base de datos y de Keycloak si existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar estado de usuario",
            description = "Permite cambiar el estado de un usuario (ej. ACTIVO/INACTIVO)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(updateUserStatusUseCase.updateStatus(id, request));
    }


    @Operation(
            summary = "Cambiar contraseña",
            description = "Actualiza la contraseña de un usuario en Keycloak."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contraseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID del usuario a modificar", required = true) @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Reiniciar contraseña",
            description = "Envía un email con el enlace para restablecer la contraseña del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Correo de reinicio enviado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido")
    })
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "ID del usuario al que se le reiniciará la contraseña", required = true)
            @PathVariable String id) {
        resetPasswordUseCase.sendResetPasswordEmail(id);
        return ResponseEntity.noContent().build();
    }
}
