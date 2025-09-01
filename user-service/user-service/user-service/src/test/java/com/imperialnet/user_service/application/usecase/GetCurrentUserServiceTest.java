package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1️⃣ Activa soporte de Mockito en JUnit 5
class GetCurrentUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort; // 2️⃣ Mock del repositorio

    @Mock
    private UserMapper userMapper; // 3️⃣ Mock del mapper

    @InjectMocks
    private GetCurrentUserService getCurrentUserService; // 4️⃣ Servicio real con mocks inyectados

    private User existingUser; // 5️⃣ Usuario de prueba
    private UserResponse expectedResponse; // 6️⃣ Respuesta esperada

    @BeforeEach
    void setUp() {
        // 7️⃣ Inicializo un usuario simulado
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

        // 8️⃣ Inicializo el DTO esperado como respuesta
        expectedResponse = new UserResponse(
                1L,
                "kc-123",
                "jdoe",
                "John",
                "Doe",
                "john.doe@example.com",
                "123456789",
                "ACTIVE"
        );
    }

    @Test
    void givenNonExistingUser_whenGetCurrentUser_thenThrowRuntimeException() {
        // 9️⃣ Configuro el mock: repo devuelve Optional vacío
        when(userRepositoryPort.findByKeycloakId("invalid-id")).thenReturn(Optional.empty());

        // 🔟 Ejecuto y espero RuntimeException
        assertThrows(RuntimeException.class,
                () -> getCurrentUserService.getCurrentUser("invalid-id"));

        // 1️⃣1️⃣ Verifico que solo se llamó al repo
        verify(userRepositoryPort).findByKeycloakId("invalid-id");
        verifyNoInteractions(userMapper);
    }

    @Test
    void givenExistingUser_whenGetCurrentUser_thenReturnUserResponse() {
        // 1️⃣2️⃣ Configuro mock: repo devuelve usuario válido
        when(userRepositoryPort.findByKeycloakId("kc-123")).thenReturn(Optional.of(existingUser));

        // 1️⃣3️⃣ Configuro mock: mapper convierte usuario en respuesta
        when(userMapper.toResponse(existingUser)).thenReturn(expectedResponse);

        // 1️⃣4️⃣ Ejecuto el método
        UserResponse response = getCurrentUserService.getCurrentUser("kc-123");

        // 1️⃣5️⃣ Verifico resultado
        assertNotNull(response);
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("jdoe", response.getUsername());

        // 1️⃣6️⃣ Verifico interacciones con mocks
        verify(userRepositoryPort).findByKeycloakId("kc-123");
        verify(userMapper).toResponse(existingUser);
    }
}
