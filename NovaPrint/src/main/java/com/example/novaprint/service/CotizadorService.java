package com.example.novaprint.service;

import com.example.novaprint.entity.Servicio;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Calcula el precio de un pedido (misma fórmula que cotizador.js).
@Service
public class CotizadorService {

    public static final BigDecimal DESCUENTO_DOBLE_CARA = new BigDecimal("0.10");

    // (precio + recargo de color) x cantidad, con 10 % menos por doble cara.
    public BigDecimal calcular(Servicio servicio, int cantidad, boolean aColor, boolean dobleCara) {
        if (servicio == null) {
            throw new IllegalArgumentException("Debes elegir un servicio");
        }
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }
        BigDecimal unitario = servicio.getPrecioUnitario();
        if (aColor) {
            unitario = unitario.add(servicio.getRecargoColor());
        }
        BigDecimal total = unitario.multiply(BigDecimal.valueOf(cantidad));
        if (dobleCara && servicio.isPermiteDobleCara()) {
            total = total.multiply(BigDecimal.ONE.subtract(DESCUENTO_DOBLE_CARA));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
