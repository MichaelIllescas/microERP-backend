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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
        log.info("📥 [POST /users] Creando usuario con email={}", request.getEmail());
        UserResponse response = createUserUseCase.createUser(request);
        log.info("✅ Usuario creado con id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve la lista completa de usuarios registrados en el sistema."
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("📥 [GET /users] Solicitando lista de usuarios");
        List<UserResponse> users = listUsersUseCase.listAll();
        log.info("✅ Se obtuvieron {} usuarios", users.size());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve la información de un usuario específico dado su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID del usuario a buscar", required = true) @PathVariable Long id) {
        log.info("📥 [GET /users/{}] Buscando usuario", id);
        UserResponse user = getUserByIdUseCase.getById(id);
        log.info("✅ Usuario encontrado: {}", user.getEmail());
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Actualizar usuario por ID",
            description = "Actualiza parcialmente los datos del usuario. Los campos nulos no se modifican."
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("📥 [PUT /users/{}] Actualizando usuario", id);
        UserResponse updated = updateUserUseCase.execute(id, request);
        log.info("✅ Usuario {} actualizado", id);
        return updated;
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina el usuario de la base de datos y de Keycloak si existe")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.warn("⚠️ [DELETE /users/{}] Eliminando usuario", id);
        deleteUserUseCase.execute(id);
        log.info("✅ Usuario {} eliminado", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar estado de usuario", description = "Permite cambiar el estado de un usuario (ej. ACTIVO/INACTIVO).")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        log.info("📥 [PATCH /users/{}/status] Actualizando estado a {}", id, request.getStatus());
        UserResponse updated = updateUserStatusUseCase.updateStatus(id, request);
        log.info("✅ Estado del usuario {} actualizado a {}", id, updated.getStatus());
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Cambiar contraseña", description = "Actualiza la contraseña de un usuario en Keycloak.")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID del usuario a modificar", required = true) @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.warn("⚠️ [PUT /users/{}/password] Cambio de contraseña solicitado", id);
        changePasswordUseCase.changePassword(id, request);
        log.info("✅ Contraseña del usuario {} actualizada", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reiniciar contraseña", description = "Envía un email con el enlace para restablecer la contraseña del usuario.")
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "ID del usuario al que se le reiniciará la contraseña", required = true)
            @PathVariable String id) {
        log.warn("⚠️ [POST /users/{}/reset-password] Reinicio de contraseña solicitado", id);
        resetPasswordUseCase.sendResetPasswordEmail(id);
        log.info("✅ Email de reinicio enviado al usuario {}", id);
        return ResponseEntity.noContent().build();
    }
}
