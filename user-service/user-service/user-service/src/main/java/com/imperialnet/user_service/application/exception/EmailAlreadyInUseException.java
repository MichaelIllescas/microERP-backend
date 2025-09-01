package com.imperialnet.user_service.application.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message) { super(message); }
}
