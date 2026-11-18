package com.example.novaprint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Datos del formulario de registro, reglas.
public record RegistroForm(
        @NotBlank(message = "Ingresa tu nombre")
        @Size(max = 100, message = "El nombre es muy largo (máximo 100)")
        String nombre,

        @NotBlank(message = "Ingresa tu correo")
        @Email(message = "El correo no es válido")
        String email,

        @NotBlank(message = "Ingresa tu teléfono")
        @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
        String telefono,

        @NotBlank(message = "Ingresa una contraseña")
        @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres")
        String password,

        @NotBlank(message = "Repite la contraseña")
        String confirmarPassword) {
}