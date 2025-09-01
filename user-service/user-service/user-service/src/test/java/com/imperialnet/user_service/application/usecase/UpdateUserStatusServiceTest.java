package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UpdateUserStatusRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
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

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private KeycloakUserPort keycloakUserPort;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UpdateUserStatusService updateUserStatusService;

    private User existingUser;
    private UpdateUserStatusRequest statusRequest;
    private User updatedUser;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
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

        statusRequest = new UpdateUserStatusRequest();
        statusRequest.setStatus("INACTIVE");

        updatedUser = existingUser.toBuilder()
                .status("INACTIVE")
                .build();

        expectedResponse = new UserResponse(
                updatedUser.getId(),
                updatedUser.getKeycloakId(),
                updatedUser.getUsername(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getStatus()
        );
    }

    @Test
    void givenNonExistingUser_whenUpdateStatus_thenThrowRuntimeException() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> updateUserStatusService.updateStatus(99L, statusRequest));

        verify(userRepositoryPort).findById(99L);
        verifyNoInteractions(keycloakUserPort, mapper);
    }

    @Test
    void givenExistingUser_whenUpdateStatus_thenUpdateDbAndKeycloak() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.save(existingUser)).thenReturn(updatedUser);
        when(mapper.toResponse(updatedUser)).thenReturn(expectedResponse);

        UserResponse response = updateUserStatusService.updateStatus(1L, statusRequest);

        assertNotNull(response);
        assertEquals("INACTIVE", response.getStatus());

        verify(userRepositoryPort).findById(1L);
        verify(keycloakUserPort).updateStatus("kc-123", "INACTIVE");
        verify(userRepositoryPort).save(existingUser);
        verify(mapper).toResponse(updatedUser);
    }
}
