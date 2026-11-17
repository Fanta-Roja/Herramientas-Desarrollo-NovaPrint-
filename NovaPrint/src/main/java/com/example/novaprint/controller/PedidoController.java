package com.example.novaprint.controller;

import com.example.novaprint.entity.Pedido;
import com.example.novaprint.entity.Servicio;
import com.example.novaprint.entity.Usuario;
import com.example.novaprint.repository.PedidoRepository;
import com.example.novaprint.repository.ServicioRepository;
import com.example.novaprint.repository.UsuarioRepository;
import com.example.novaprint.service.CotizadorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Pedidos de los clientes.
@Controller
public class PedidoController {

    private static final int CANTIDAD_MAXIMA = 10000;

    private final PedidoRepository pedidoRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final CotizadorService cotizador;

    public PedidoController(PedidoRepository pedidoRepository, ServicioRepository servicioRepository,
                            UsuarioRepository usuarioRepository, CotizadorService cotizador) {
        this.pedidoRepository = pedidoRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.cotizador = cotizador;
    }

    // Formulario de nuevo pedido.
    @GetMapping("/pedidos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "pedido-nuevo";
    }

    // Valida el pedido, calcula el total y lo guarda.
    @PostMapping("/pedidos/guardar")
    public String guardar(@RequestParam Long servicioId,
                          @RequestParam int cantidad,
                          @RequestParam(defaultValue = "false") boolean color,
                          @RequestParam(defaultValue = "false") boolean dobleCara,
                          @RequestParam(required = false) String notas,
                          Authentication auth,
                          RedirectAttributes redirect) {
        Servicio servicio = servicioRepository.findById(servicioId).orElse(null);
        if (servicio == null || cantidad < 1 || cantidad > CANTIDAD_MAXIMA) {
            redirect.addAttribute("error", true);
            return "redirect:/pedidos/nuevo";
        }
        Usuario usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();

        Pedido p = new Pedido();
        p.setUsuario(usuario);
        p.setServicio(servicio);
        p.setCantidad(cantidad);
        p.setColor(color);
        p.setDobleCara(dobleCara && servicio.isPermiteDobleCara());
        p.setNotas(notas == null ? null : (notas.length() > 500 ? notas.substring(0, 500) : notas));
        p.setTotal(cotizador.calcular(servicio, cantidad, color, p.isDobleCara()));
        pedidoRepository.save(p);

        redirect.addAttribute("creado", true);
        return "redirect:/mis-pedidos";
    }

    // Lista los pedidos del cliente.
    @GetMapping("/mis-pedidos")
    public String misPedidos(Authentication auth, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("pedidos", pedidoRepository.findByUsuarioOrderByFechaDesc(usuario));
        return "mis-pedidos";
    }
}
