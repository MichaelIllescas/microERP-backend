package com.imperialnet.user_service;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador básico para pruebas del User Service.
 * Expone un endpoint GET de prueba.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "✅ User Service funcionando correctamente!";
    }
}