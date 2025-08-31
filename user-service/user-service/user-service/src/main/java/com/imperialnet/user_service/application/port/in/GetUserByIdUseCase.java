// application/port/in/GetUserByIdUseCase.java
package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.UserResponse;

/**
 * Puerto de entrada para obtener un usuario por su ID.
 */
public interface GetUserByIdUseCase {

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return UserResponse con los datos del usuario
     */
    UserResponse getById(Long id);
}
