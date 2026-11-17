package com.example.novaprint.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.novaprint.entity.Servicio;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CotizadorServiceTest {

    private final CotizadorService cotizador = new CotizadorService();
    private final Servicio fotocopias = new Servicio("Fotocopias", "d", "fa-copy", "0.10", "0.40", true);
    private final Servicio escaneo = new Servicio("Escaneo", "d", "fa-file-import", "0.50", "0.00", false);

    @Test
    void blancoYNegro() {
        assertEquals(new BigDecimal("10.00"), cotizador.calcular(fotocopias, 100, false, false));
    }

    @Test
    void aColorSumaRecargo() {
        assertEquals(new BigDecimal("50.00"), cotizador.calcular(fotocopias, 100, true, false));
    }

    @Test
    void dobleCaraAplicaDescuentoDelDiezPorCiento() {
        assertEquals(new BigDecimal("9.00"), cotizador.calcular(fotocopias, 100, false, true));
    }

    @Test
    void dobleCaraSeIgnoraSiElServicioNoLoPermite() {
        assertEquals(new BigDecimal("5.00"), cotizador.calcular(escaneo, 10, false, true));
    }

    @Test
    void cantidadInvalidaLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> cotizador.calcular(fotocopias, 0, false, false));
    }
}
