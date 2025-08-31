// application/port/in/ListUsersUseCase.java
package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.UserResponse;
import java.util.List;

/**
 * Puerto de entrada para listar todos los usuarios.
 */
public interface ListUsersUseCase {
    List<UserResponse> listAll();
}
