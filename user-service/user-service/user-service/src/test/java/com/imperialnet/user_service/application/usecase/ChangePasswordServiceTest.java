package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.ChangePasswordRequest;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1️⃣ Habilita soporte de Mockito en JUnit 5
class ChangePasswordServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort; // 2️⃣ Mock para el repositorio (DB local)

    @Mock
    private KeycloakUserPort keycloakUserPort; // 3️⃣ Mock para la integración con Keycloak

    @InjectMocks
    private ChangePasswordService changePasswordService; // 4️⃣ Inyecta los mocks dentro del servicio real

    private User existingUser; // 5️⃣ Usuario de ejemplo existente en DB
    private ChangePasswordRequest request; // 6️⃣ Petición de cambio de contraseña

    @BeforeEach
    void setUp() {
        // 7️⃣ Construyo un usuario válido que simula estar en DB
        existingUser = User.builder()
                .id(1L)
                .keycloakId("kc-123")
                .username("jdoe")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .status("ACTIVE")
                .build();

        // 8️⃣ Construyo una petición de cambio de contraseña válida
        request = new ChangePasswordRequest("NewSecurePassword123!");
    }

    @Test
    void givenNonExistingUser_whenChangePassword_thenThrowRuntimeException() {
        // 9️⃣ Configuro el mock: al buscar por id=99 no encuentra usuario
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // 🔟 Ejecuto el método y espero que lance RuntimeException
        assertThrows(RuntimeException.class,
                () -> changePasswordService.changePassword(99L, request));

        // 1️⃣1️⃣ Verifico que se llamó a findById
        verify(userRepositoryPort).findById(99L);

        // 1️⃣2️⃣ Verifico que NO se llamó a Keycloak (porque el usuario no existe)
        verifyNoInteractions(keycloakUserPort);
    }

    @Test
    void givenExistingUser_whenChangePassword_thenInvokeKeycloak() {
        // 1️⃣3️⃣ Configuro el mock: al buscar por id=1 devuelve existingUser
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existingUser));

        // 1️⃣4️⃣ Ejecuto el método con usuario válido y request válido
        changePasswordService.changePassword(1L, request);

        // 1️⃣5️⃣ Verifico que se llamó a findById con id=1
        verify(userRepositoryPort).findById(1L);

        // 1️⃣6️⃣ Verifico que se llamó a Keycloak con el id y la nueva contraseña
        verify(keycloakUserPort).updatePassword("kc-123", "NewSecurePassword123!");
    }
}
