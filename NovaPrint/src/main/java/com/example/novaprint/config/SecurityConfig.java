package com.example.novaprint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define las páginas públicas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/registro", "/procesarRegistro", "/procesarContacto",
                        "/css/**", "/js/**", "/img/**", "/error", "/actuator/health").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/procesarLogin")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
                // Confirmacion de cuenta bloqueada
                .failureHandler((request, response, excepcion) -> response.sendRedirect(
                        request.getContextPath()
                                + (excepcion instanceof LockedException ? "/login?bloqueado" : "/login?error")))
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll());
        return http.build();
    }
}