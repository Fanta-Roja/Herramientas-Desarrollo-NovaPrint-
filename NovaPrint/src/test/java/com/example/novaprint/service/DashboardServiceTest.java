package com.example.novaprint.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.novaprint.entity.EstadoPedido;
import com.example.novaprint.entity.Pedido;
import com.example.novaprint.entity.Rol;
import com.example.novaprint.entity.Servicio;
import com.example.novaprint.entity.Usuario;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class DashboardServiceTest {

    private final DashboardService service = new DashboardService();
    private final Servicio fotocopias = new Servicio("Fotocopias", "d", "fa-copy", "0.10", "0.40", true);
    private final Servicio escaneo = new Servicio("Escaneo", "d", "fa-file-import", "0.50", "0.00", false);

    private Pedido pedido(Servicio s, String total, EstadoPedido estado, int minutos) {
        Pedido p = new Pedido();
        p.setServicio(s);
        p.setTotal(new BigDecimal(total));
        p.setEstado(estado);
        p.setFecha(LocalDateTime.of(2026, 1, 1, 10, 0).plusMinutes(minutos));
        return p;
    }

    private Usuario usuario(Rol rol) {
        Usuario u = new Usuario();
        u.setRol(rol);
        return u;
    }

    @Test
    void calculaTotalesVentasYClientes() {
        var resumen = service.construir(List.of(
                pedido(fotocopias, "10.00", EstadoPedido.RECIBIDO, 1),
                pedido(fotocopias, "5.00", EstadoPedido.ENTREGADO, 2),
                pedido(escaneo, "20.00", EstadoPedido.CANCELADO, 3)),
                List.of(usuario(Rol.ADMIN), usuario(Rol.USER), usuario(Rol.USER)));

        assertEquals(3, resumen.getTotalPedidos());
        assertEquals(1, resumen.getPendientes());
        assertEquals(new BigDecimal("15.00"), resumen.getVentas());
        assertEquals(2, resumen.getClientes());
    }

    @Test
    void ordenaLasBarrasPorServicioYCalculaPorcentajes() {
        var resumen = service.construir(List.of(
                pedido(escaneo, "1.00", EstadoPedido.RECIBIDO, 1),
                pedido(fotocopias, "1.00", EstadoPedido.RECIBIDO, 2),
                pedido(fotocopias, "1.00", EstadoPedido.RECIBIDO, 3),
                pedido(fotocopias, "1.00", EstadoPedido.RECIBIDO, 4)), List.of());

        assertEquals("Fotocopias", resumen.getPorServicio().get(0).getEtiqueta());
        assertEquals(100, resumen.getPorServicio().get(0).getPorcentaje());
        assertEquals(33, resumen.getPorServicio().get(1).getPorcentaje());
    }

    @Test
    void sinPedidosTodoQuedaEnCero() {
        var resumen = service.construir(List.of(), List.of());

        assertEquals(0, resumen.getTotalPedidos());
        assertEquals(BigDecimal.ZERO, resumen.getVentas());
        assertEquals(EstadoPedido.values().length, resumen.getPorEstado().size());
        assertEquals(0, resumen.getPorEstado().get(0).getPorcentaje());
    }

    @Test
    void losUltimosPedidosSonCincoYLosMasRecientesPrimero() {
        var pedidos = new java.util.ArrayList<Pedido>();
        for (int i = 1; i <= 7; i++) pedidos.add(pedido(fotocopias, "1.00", EstadoPedido.RECIBIDO, i));

        var ultimos = service.construir(pedidos, List.of()).getUltimos();

        assertEquals(5, ultimos.size());
        assertEquals(pedidos.get(6), ultimos.get(0));
    }
}
