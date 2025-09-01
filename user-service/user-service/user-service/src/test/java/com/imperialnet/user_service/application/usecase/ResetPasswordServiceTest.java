package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
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

@ExtendWith(MockitoExtension.class) // 1️⃣ Habilita Mockito con JUnit 5
class ResetPasswordServiceTest {

    @Mock
    private KeycloakUserPort keycloakUserPort; // 2️⃣ Mock de integración con Keycloak

    @Mock
    private UserRepositoryPort userRepositoryPort; // 3️⃣ Mock del repositorio (DB local)

    @InjectMocks
    private ResetPasswordService resetPasswordService; // 4️⃣ Servicio real con mocks inyectados

    private User existingUser; // 5️⃣ Usuario de ejemplo válido

    @BeforeEach
    void setUp() {
        // 6️⃣ Inicializo un usuario válido
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
    }

    @Test
    void givenInvalidUserIdFormat_whenSendResetPasswordEmail_thenThrowIllegalArgumentException() {
        // 7️⃣ Paso un id no numérico
        String invalidId = "abc";

        // 8️⃣ Espero IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> resetPasswordService.sendResetPasswordEmail(invalidId));

        // 9️⃣ Verifico que no se llamó ni a la DB ni a Keycloak
        verifyNoInteractions(userRepositoryPort, keycloakUserPort);
    }

    @Test
    void givenNonExistingUser_whenSendResetPasswordEmail_thenThrowResourceNotFoundException() {
        // 🔟 Paso un id numérico que no existe
        String nonExistingId = "99";

        // 1️⃣1️⃣ Configuro el mock para devolver vacío
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // 1️⃣2️⃣ Espero ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> resetPasswordService.sendResetPasswordEmail(nonExistingId));

        // 1️⃣3️⃣ Verifico que se consultó la DB
        verify(userRepositoryPort).findById(99L);

        // 1️⃣4️⃣ Verifico que nunca se llamó a Keycloak
        verifyNoInteractions(keycloakUserPort);
    }

    @Test
    void givenExistingUser_whenSendResetPasswordEmail_thenInvokeKeycloak() {
        // 1️⃣5️⃣ Paso un id válido que sí existe
        String existingId = "1";

        // 1️⃣6️⃣ Configuro el mock para devolver el usuario
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existingUser));

        // 1️⃣7️⃣ Ejecuto el método
        resetPasswordService.sendResetPasswordEmail(existingId);

        // 1️⃣8️⃣ Verifico que se consultó la DB
        verify(userRepositoryPort).findById(1L);

        // 1️⃣9️⃣ Verifico que se llamó a Keycloak con el keycloakId correcto
        verify(keycloakUserPort).sendResetPasswordEmail("kc-123");
    }
}
