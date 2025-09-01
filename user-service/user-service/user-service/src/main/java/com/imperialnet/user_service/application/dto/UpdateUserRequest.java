package com.imperialnet.user_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "UpdateUserRequest", description = "Campos opcionales para actualizar un usuario")
public class UpdateUserRequest {

    @Schema(description = "Nombre del usuario", example = "Juan")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    private String lastName;

    @Schema(description = "Email del usuario", example = "juan.perez@empresa.com")
    @Email(message = "Formato de email inválido")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "+54 9 11 5555 5555")
    @Size(max = 30, message = "El teléfono no debe superar 30 caracteres")
    private String phone;
}
