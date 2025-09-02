package com.imperialnet.customer_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de dominio que representa un Cliente.
 * No depende de frameworks ni de JPA.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    private Long id;          // Identificador local
    private String firstName; // Nombre
    private String lastName;  // Apellido
    private String email;     // Email único
    private String phone;     // Teléfono de contacto
    private String address;   // Dirección
    private String status;    // Estado del cliente (ej: ACTIVE, INACTIVE)
}
