package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.CreateUserRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private KeycloakUserPort keycloakUserPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CreateUserService createUserService;

    private CreateUserRequest validRequest;
    private User userDomain;
    private User savedUser;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserRequest();
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("StrongPass123!");
        validRequest.setPhone("123456789");

        userDomain = User.builder()
                .id(null)
                .username("test") // derivado del email
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phone("123456789")
                .status("ACTIVE")
                .build();

        savedUser = userDomain.toBuilder()
                .id(1L)
                .keycloakId("kc-123")
                .build();

        expectedResponse = new UserResponse(
                1L,
                "kc-123",
                "test",
                "John",
                "Doe",
                "test@example.com",
                "123456789",
                "ACTIVE"
        );
    }

    @Test
    void givenMissingEmail_whenCreateUser_thenThrowIllegalArgumentException() {
        // Arrange
        CreateUserRequest requestWithoutEmail = new CreateUserRequest();
        requestWithoutEmail.setFirstName("John");
        requestWithoutEmail.setLastName("Doe");
        requestWithoutEmail.setPassword("StrongPass123!");
        requestWithoutEmail.setPhone("123456789");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createUserService.createUser(requestWithoutEmail));

        // No se debería invocar a los mocks
        verifyNoInteractions(keycloakUserPort, userRepositoryPort, userMapper);
    }

    @Test
    void givenMissingPassword_whenCreateUser_thenThrowIllegalArgumentException() {
        // Arrange
        CreateUserRequest requestWithoutPassword = new CreateUserRequest();
        requestWithoutPassword.setFirstName("John");
        requestWithoutPassword.setLastName("Doe");
        requestWithoutPassword.setEmail("test@example.com");
        requestWithoutPassword.setPhone("123456789");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createUserService.createUser(requestWithoutPassword));

        verifyNoInteractions(keycloakUserPort, userRepositoryPort, userMapper);
    }

    @Test
    void givenValidRequest_whenCreateUser_thenSaveInDbAndKeycloak() {
        // Arrange
        when(userMapper.toDomain(validRequest)).thenReturn(userDomain);
        when(keycloakUserPort.createUser(userDomain, validRequest.getPassword())).thenReturn("kc-123");
        when(userRepositoryPort.save(userDomain)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        // Act
        UserResponse response = createUserService.createUser(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getEmail(), response.getEmail());
        assertEquals(expectedResponse.getFirstName(), response.getFirstName());

        // Verify
        verify(userMapper).toDomain(validRequest);
        verify(keycloakUserPort).createUser(userDomain, validRequest.getPassword());
        verify(userRepositoryPort).save(userDomain);
        verify(userMapper).toResponse(savedUser);
    }
}
