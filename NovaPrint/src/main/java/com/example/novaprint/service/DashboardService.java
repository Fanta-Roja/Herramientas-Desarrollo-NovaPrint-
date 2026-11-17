package com.example.novaprint.service;

import com.example.novaprint.entity.EstadoPedido;
import com.example.novaprint.entity.Pedido;
import com.example.novaprint.entity.Rol;
import com.example.novaprint.entity.Usuario;
import com.example.novaprint.service.ResumenDashboard.Barra;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Calcula las estadísticas del dashboard a partir de los pedidos y usuarios.
@Service
public class DashboardService {

    private static final int ULTIMOS_PEDIDOS = 5;

    // Arma el resumen: totales, ventas, gráficos por servicio y por estado, y últimos pedidos.
    public ResumenDashboard construir(List<Pedido> pedidos, List<Usuario> usuarios) {
        long pendientes = pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.RECIBIDO || p.getEstado() == EstadoPedido.EN_PROCESO)
                .count();
        BigDecimal ventas = pedidos.stream()
                .filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long clientes = usuarios.stream().filter(u -> u.getRol() == Rol.USER).count();

        Map<String, Long> servicios = new LinkedHashMap<>();
        pedidos.forEach(p -> servicios.merge(p.getServicio().getNombre(), 1L, Long::sum));
        Map<String, Long> estados = new LinkedHashMap<>();
        for (EstadoPedido e : EstadoPedido.values()) {
            estados.put(e.getEtiqueta(), pedidos.stream().filter(p -> p.getEstado() == e).count());
        }

        List<Pedido> ultimos = pedidos.stream()
                .sorted(Comparator.comparing(Pedido::getFecha).reversed())
                .limit(ULTIMOS_PEDIDOS)
                .toList();

        return new ResumenDashboard(pedidos.size(), pendientes, ventas, clientes,
                barras(servicios, true), barras(estados, false), ultimos);
    }

    // Convierte conteos en barras con porcentaje; si se pide, las ordena de mayor a menor.
    private List<Barra> barras(Map<String, Long> conteos, boolean ordenar) {
        List<Map.Entry<String, Long>> entradas = new ArrayList<>(conteos.entrySet());
        if (ordenar) {
            entradas.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        }
        long maximo = entradas.stream().mapToLong(Map.Entry::getValue).max().orElse(0);
        List<Barra> barras = new ArrayList<>();
        for (Map.Entry<String, Long> e : entradas) {
            int porcentaje = maximo == 0 ? 0 : (int) (e.getValue() * 100 / maximo);
            barras.add(new Barra(e.getKey(), e.getValue(), porcentaje));
        }
        return barras;
    }
}
