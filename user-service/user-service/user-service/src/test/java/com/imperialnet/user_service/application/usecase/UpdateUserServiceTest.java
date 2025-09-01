package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UpdateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.exception.EmailAlreadyInUseException;
import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
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
class UpdateUserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private KeycloakUserPort keycloakPort;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UpdateUserService updateUserService;

    private User existingUser;
    private UpdateUserRequest validRequest;

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

        validRequest = new UpdateUserRequest();
        validRequest.setFirstName("Johnny");
        validRequest.setLastName("Doe");
        validRequest.setEmail("johnny.doe@example.com");
        validRequest.setPhone("987654321");
    }

    @Test
    void givenNonExistingUser_whenUpdateUser_thenThrowResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> updateUserService.execute(99L, validRequest));

        verify(userRepository).findById(99L);
        verifyNoMoreInteractions(userRepository, keycloakPort, userMapper);
    }

    @Test
    void givenEmailAlreadyInUse_whenUpdateUser_thenThrowEmailAlreadyInUseException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(validRequest.getEmail(), 1L)).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class,
                () -> updateUserService.execute(1L, validRequest));

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndIdNot(validRequest.getEmail(), 1L);
        verifyNoMoreInteractions(userRepository, keycloakPort, userMapper);
    }

    @Test
    void givenValidRequest_whenUpdateUser_thenUpdateDbAndKeycloak() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(validRequest.getEmail(), 1L)).thenReturn(false);

        User updatedUser = existingUser.toBuilder()
                .firstName(validRequest.getFirstName())
                .lastName(validRequest.getLastName())
                .email(validRequest.getEmail())
                .phone(validRequest.getPhone())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponse expectedResponse = new UserResponse(
                updatedUser.getId(),
                updatedUser.getKeycloakId(),
                updatedUser.getUsername(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getStatus()
        );

        when(userMapper.toResponse(updatedUser)).thenReturn(expectedResponse);

        UserResponse response = updateUserService.execute(1L, validRequest);

        assertNotNull(response);
        assertEquals("johnny.doe@example.com", response.getEmail());
        assertEquals("Johnny", response.getFirstName());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndIdNot(validRequest.getEmail(), 1L);
        verify(userRepository).save(any(User.class));
        verify(keycloakPort).updateEmail("kc-123", "johnny.doe@example.com");
        verify(keycloakPort).updateProfile("kc-123", "Johnny", "987654321");
        verify(userMapper).toResponse(updatedUser);
    }
}
