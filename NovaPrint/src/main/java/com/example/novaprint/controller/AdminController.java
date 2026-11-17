package com.example.novaprint.controller;

import com.example.novaprint.entity.EstadoPedido;
import com.example.novaprint.repository.ContactoRepository;
import com.example.novaprint.repository.PedidoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Panel del administrador (solo rol ADMIN).
@Controller
public class AdminController {

    private final PedidoRepository pedidoRepository;
    private final ContactoRepository contactoRepository;

    public AdminController(PedidoRepository pedidoRepository, ContactoRepository contactoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.contactoRepository = contactoRepository;
    }

    // Lista todos los pedidos.
    @GetMapping("/admin/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("pedidos", pedidoRepository.findAllByOrderByFechaDesc());
        model.addAttribute("estados", EstadoPedido.values());
        return "admin-pedidos";
    }

    // Cambia el estado de un pedido.
    @PostMapping("/admin/pedidos/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        pedidoRepository.findById(id).ifPresent(p -> {
            p.setEstado(estado);
            pedidoRepository.save(p);
        });
        return "redirect:/admin/pedidos";
    }

    // Lista los mensajes de contacto.
    @GetMapping("/admin/mensajes")
    public String mensajes(Model model) {
        model.addAttribute("mensajes", contactoRepository.findAllByOrderByFechaEnvioDesc());
        return "admin-mensajes";
    }
}
