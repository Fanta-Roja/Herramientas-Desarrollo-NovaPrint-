package com.example.novaprint.service;

import com.example.novaprint.entity.Pedido;
import java.math.BigDecimal;
import java.util.List;

// Datos que muestra el dashboard del administrador.
public class ResumenDashboard {

    // Una barra del gráfico: etiqueta, cantidad y porcentaje respecto a la barra más grande.
    public static class Barra {
        private final String etiqueta;
        private final long valor;
        private final int porcentaje;

        public Barra(String etiqueta, long valor, int porcentaje) {
            this.etiqueta = etiqueta;
            this.valor = valor;
            this.porcentaje = porcentaje;
        }

        public String getEtiqueta() { return etiqueta; }
        public long getValor() { return valor; }
        public int getPorcentaje() { return porcentaje; }
    }

    private final int totalPedidos;
    private final long pendientes;
    private final BigDecimal ventas;
    private final long clientes;
    private final List<Barra> porServicio;
    private final List<Barra> porEstado;
    private final List<Pedido> ultimos;

    public ResumenDashboard(int totalPedidos, long pendientes, BigDecimal ventas, long clientes,
                            List<Barra> porServicio, List<Barra> porEstado, List<Pedido> ultimos) {
        this.totalPedidos = totalPedidos;
        this.pendientes = pendientes;
        this.ventas = ventas;
        this.clientes = clientes;
        this.porServicio = porServicio;
        this.porEstado = porEstado;
        this.ultimos = ultimos;
    }

    public int getTotalPedidos() { return totalPedidos; }
    public long getPendientes() { return pendientes; }
    public BigDecimal getVentas() { return ventas; }
    public long getClientes() { return clientes; }
    public List<Barra> getPorServicio() { return porServicio; }
    public List<Barra> getPorEstado() { return porEstado; }
    public List<Pedido> getUltimos() { return ultimos; }
}
