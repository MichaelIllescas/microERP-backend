package com.imperialnet.user_service.infrastructure.adapter.in;


import com.imperialnet.user_service.application.dto.UserResponse;

import com.imperialnet.user_service.application.port.in.GetCurrentUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class GetCurrentUserController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    public GetCurrentUserController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        // Extraemos el "sub" (ID único del usuario en Keycloak)
        String keycloakId = jwt.getSubject();
        return getCurrentUserUseCase.getCurrentUser(keycloakId);
    }
}
