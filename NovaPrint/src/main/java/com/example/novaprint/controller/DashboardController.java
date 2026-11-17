package com.example.novaprint.controller;

import com.example.novaprint.entity.Rol;
import com.example.novaprint.repository.PedidoRepository;
import com.example.novaprint.repository.UsuarioRepository;
import com.example.novaprint.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Dashboard del administrador (solo rol ADMIN).
@Controller
public class DashboardController {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DashboardService dashboardService;

    public DashboardController(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository,
                               DashboardService dashboardService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.dashboardService = dashboardService;
    }

    // Muestra las estadísticas, los gráficos y la lista de usuarios.
    @GetMapping("/admin/dashboard")
    public String dashboard(Authentication auth, Model model) {
        var usuarios = usuarioRepository.findAll();
        model.addAttribute("resumen", dashboardService.construir(pedidoRepository.findAll(), usuarios));
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("emailActual", auth.getName());
        return "admin-dashboard";
    }

    // Cambia el rol de un usuario entre USER y ADMIN (no permite cambiar el propio).
    @PostMapping("/admin/usuarios/{id}/rol")
    public String cambiarRol(@PathVariable Long id, Authentication auth) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (!u.getEmail().equals(auth.getName())) {
                u.setRol(u.getRol() == Rol.ADMIN ? Rol.USER : Rol.ADMIN);
                usuarioRepository.save(u);
            }
        });
        return "redirect:/admin/dashboard";
    }
}
