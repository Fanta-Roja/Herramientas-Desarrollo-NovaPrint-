package com.example.novaprint.config;

import com.example.novaprint.service.LoginAttemptService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// Analiza los intentos y exitoso para llevar la cuenta de intentos.
@Component
public class LoginEventos {

    private final LoginAttemptService intentos;

    public LoginEventos(LoginAttemptService intentos) {
        this.intentos = intentos;
    }

    @EventListener
    public void alFallar(AuthenticationFailureBadCredentialsEvent evento) {
        intentos.registrarFallo(normalizar(evento.getAuthentication().getName()));
    }

    @EventListener
    public void alIngresar(AuthenticationSuccessEvent evento) {
        intentos.registrarExito(normalizar(evento.getAuthentication().getName()));
    }

    private String normalizar(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}