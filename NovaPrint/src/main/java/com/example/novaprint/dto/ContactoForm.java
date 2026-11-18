package com.example.novaprint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Datos del formulario 
public record ContactoForm(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Email String email,
        @Size(max = 20) String telefono,
        @NotBlank @Size(max = 1000) String mensaje) {
}