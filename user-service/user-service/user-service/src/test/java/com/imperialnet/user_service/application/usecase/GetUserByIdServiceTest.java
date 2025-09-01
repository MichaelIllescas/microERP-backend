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
class GetUserByIdServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort; // 2️⃣ Mock del repositorio

    @Mock
    private UserMapper userMapper; // 3️⃣ Mock del mapper

    @InjectMocks
    private GetUserByIdService getUserByIdService; // 4️⃣ Servicio real con mocks inyectados

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
    void givenNonExistingUser_whenGetById_thenThrowRuntimeException() {
        // 9️⃣ Configuro mock: repo devuelve Optional vacío
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // 🔟 Ejecuto y espero RuntimeException
        assertThrows(RuntimeException.class,
                () -> getUserByIdService.getById(99L));

        // 1️⃣1️⃣ Verifico interacción con el repo
        verify(userRepositoryPort).findById(99L);

        // 1️⃣2️⃣ Verifico que nunca se llamó al mapper
        verifyNoInteractions(userMapper);
    }

    @Test
    void givenExistingUser_whenGetById_thenReturnUserResponse() {
        // 1️⃣3️⃣ Configuro mock: repo devuelve usuario válido
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existingUser));

        // 1️⃣4️⃣ Configuro mock: mapper convierte a respuesta
        when(userMapper.toResponse(existingUser)).thenReturn(expectedResponse);

        // 1️⃣5️⃣ Ejecuto el método
        UserResponse response = getUserByIdService.getById(1L);

        // 1️⃣6️⃣ Verifico resultado
        assertNotNull(response);
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("jdoe", response.getUsername());

        // 1️⃣7️⃣ Verifico interacciones correctas
        verify(userRepositoryPort).findById(1L);
        verify(userMapper).toResponse(existingUser);
    }
}
