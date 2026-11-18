package com.example.novaprint.controller;

import com.example.novaprint.dto.ContactoForm;
import com.example.novaprint.dto.RegistroForm;
import com.example.novaprint.entity.Contacto;
import com.example.novaprint.entity.Usuario;
import com.example.novaprint.repository.ContactoRepository;
import com.example.novaprint.repository.ServicioRepository;
import com.example.novaprint.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Páginas públicas: portada, login, registro y contacto.
@Controller
public class PublicController {

    private final UsuarioRepository usuarioRepository;
    private final ContactoRepository contactoRepository;
    private final ServicioRepository servicioRepository;
    private final PasswordEncoder passwordEncoder;

    public PublicController(UsuarioRepository usuarioRepository, ContactoRepository contactoRepository,
                            ServicioRepository servicioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.contactoRepository = contactoRepository;
        this.servicioRepository = servicioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Portada con la lista de servicios.
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "index";
    }

    // Formulario de inicio de sesión.
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Formulario de registro (con un objeto vacío para enlazar los campos).
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("form", new RegistroForm("", "", "", "", ""));
        return "registro";
    }

    // Valida los datos, muestra los errores junto a cada campo y crea el usuario.
    @PostMapping("/procesarRegistro")
    public String procesarRegistro(@Valid @ModelAttribute("form") RegistroForm form,
                                   BindingResult result) {
        String email = form.email() == null ? "" : form.email().trim().toLowerCase();

        if (!result.hasErrors() && !form.password().equals(form.confirmarPassword())) {
            result.rejectValue("confirmarPassword", "noCoincide", "Las contraseñas no coinciden.");
        }
        if (!result.hasErrors() && usuarioRepository.existsByEmail(email)) {
            result.rejectValue("email", "duplicado", "Ese correo ya está registrado. Prueba iniciando sesión.");
        }
        if (result.hasErrors()) {
            return "registro";
        }

        Usuario u = new Usuario();
        u.setNombre(form.nombre().trim());
        u.setEmail(email);
        u.setTelefono(form.telefono().trim());
        u.setPassword(passwordEncoder.encode(form.password()));
        usuarioRepository.save(u);
        return "redirect:/login?registroExitoso";
    }

    // Valida y guarda el mensaje del formulario de contacto.
    @PostMapping("/procesarContacto")
    public String procesarContacto(@Valid ContactoForm form, BindingResult result,
                                   RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addAttribute("errorContacto", true);
            return "redirect:/#contacto";
        }
        Contacto c = new Contacto();
        c.setNombre(form.nombre().trim());
        c.setEmail(form.email().trim().toLowerCase());
        c.setTelefono(form.telefono() == null || form.telefono().isBlank() ? null : form.telefono().trim());
        c.setMensaje(form.mensaje().trim());
        contactoRepository.save(c);
        redirect.addAttribute("exito", true);
        return "redirect:/#contacto";
    }
}