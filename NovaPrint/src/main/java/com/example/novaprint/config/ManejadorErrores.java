package com.example.novaprint.config;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

// Visibilidad de la PG
@ControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noEncontrado(Model model) {
        model.addAttribute("mensaje", "No encontramos lo que buscabas.");
        return "error";
    }
}