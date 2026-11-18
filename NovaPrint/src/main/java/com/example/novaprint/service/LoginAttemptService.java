package com.example.novaprint.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Cuenta las contraseñas incorrectas por correo y bloquea la cuenta un tiempo
@Service
public class LoginAttemptService {

    // Intentos fallidos de un correo y fecha de aviso de bloqueo
    private record Registro(int intentos, Instant bloqueadoHasta) {}

    private final Map<String, Registro> registros = new ConcurrentHashMap<>();
    private final int maxIntentos;
    private final Duration duracionBloqueo;

    public LoginAttemptService(@Value("${app.login.max-intentos:5}") int maxIntentos,
                               @Value("${app.login.bloqueo-minutos:15}") long bloqueoMinutos) {
        this.maxIntentos = maxIntentos;
        this.duracionBloqueo = Duration.ofMinutes(bloqueoMinutos);
    }

    // Suma un intento fallido
    public void registrarFallo(String email) {
        registros.compute(email, (k, r) -> {
            int intentos = (r == null ? 0 : r.intentos()) + 1;
            Instant hasta = intentos >= maxIntentos ? Instant.now().plus(duracionBloqueo) : null;
            return new Registro(intentos, hasta);
        });
    }

    // Un ingreso correcto borra el contador
    public void registrarExito(String email) {
        registros.remove(email);
    }

    // Indica si la cuenta sigue bloqueada
    public boolean estaBloqueado(String email) {
        Registro r = registros.get(email);
        if (r == null || r.bloqueadoHasta() == null) {
            return false;
        }
        if (Instant.now().isAfter(r.bloqueadoHasta())) {
            registros.remove(email);
            return false;
        }
        return true;
    }
}