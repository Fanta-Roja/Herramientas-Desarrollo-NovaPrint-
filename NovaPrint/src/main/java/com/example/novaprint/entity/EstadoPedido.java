package com.example.novaprint.entity;

public enum EstadoPedido {
    RECIBIDO("Recibido"), EN_PROCESO("En proceso"), LISTO("Listo para recoger"), ENTREGADO("Entregado"), CANCELADO("Cancelado");

    private final String etiqueta;

    EstadoPedido(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
