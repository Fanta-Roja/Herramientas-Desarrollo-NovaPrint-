package com.example.novaprint.service;

import com.example.novaprint.entity.Usuario;
import com.example.novaprint.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Busca al usuario y la marca si anda bloqueado
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final LoginAttemptService intentos;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, LoginAttemptService intentos) {
        this.usuarioRepository = usuarioRepository;
        this.intentos = intentos;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Correo de busqueda
        String correo = email == null ? "" : email.trim().toLowerCase();
        Usuario u = usuarioRepository.findByEmail(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRol().name())
                .accountLocked(intentos.estaBloqueado(u.getEmail()))
                .build();
    }
}