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

@ExtendWith(MockitoExtension.class) // 1️⃣ Activa Mockito en JUnit 5
class DeleteUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository; // 2️⃣ Mock del repositorio (BD local)

    @Mock
    private KeycloakUserPort keycloakPort; // 3️⃣ Mock del adaptador a Keycloak

    @InjectMocks
    private DeleteUserService deleteUserService; // 4️⃣ Servicio real con mocks inyectados

    private User existingUser; // 5️⃣ Usuario válido de prueba

    @BeforeEach
    void setUp() {
        // 6️⃣ Inicializo un usuario válido como si existiera en DB
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
    void givenNonExistingUser_whenDeleteUser_thenThrowResourceNotFoundException() {
        // 7️⃣ Configuro el mock: al buscar id=99 devuelve Optional vacío
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // 8️⃣ Ejecuto el método y espero ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> deleteUserService.execute(99L));

        // 9️⃣ Verifico que se llamó al repositorio
        verify(userRepository).findById(99L);

        // 🔟 Verifico que NO se llamó a Keycloak ni a deleteById
        verifyNoInteractions(keycloakPort);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void givenExistingUser_whenDeleteUser_thenRemoveFromDbAndKeycloak() {
        // 1️⃣1️⃣ Configuro el mock: al buscar id=1 devuelve existingUser
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // 1️⃣2️⃣ Ejecuto el método
        deleteUserService.execute(1L);

        // 1️⃣3️⃣ Verifico que se buscó en DB
        verify(userRepository).findById(1L);

        // 1️⃣4️⃣ Verifico que se llamó a Keycloak para eliminar
        verify(keycloakPort).deleteUser("kc-123");

        // 1️⃣5️⃣ Verifico que se eliminó en la DB local
        verify(userRepository).deleteById(1L);
    }
}
