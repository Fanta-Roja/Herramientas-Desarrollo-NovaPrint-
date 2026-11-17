// Calcula el total en pantalla (misma fórmula que CotizadorService).
(function () {
    const DESCUENTO_DOBLE_CARA = 0.10;

    document.querySelectorAll('.js-cotizador').forEach(function (caja) {
        const servicio = caja.querySelector('[name=servicioId]');
        const cantidad = caja.querySelector('[name=cantidad]');
        const color = caja.querySelector('[name=color]');
        const doble = caja.querySelector('[name=dobleCara]');
        const grupoColor = caja.querySelector('#grupoColor');
        const grupoDoble = caja.querySelector('#grupoDoble');
        const salida = caja.querySelector('.js-total');
        if (!servicio || !cantidad || !salida) return;

        function calcular() {
            const op = servicio.options[servicio.selectedIndex];
            const precio = parseFloat(op.dataset.precio);
            const recargo = parseFloat(op.dataset.recargo);
            const permiteDoble = op.dataset.doble === 'true';
            const tieneColor = recargo > 0;

            // Oculta las opciones que el servicio no admite.
            grupoColor.style.display = tieneColor ? '' : 'none';
            grupoDoble.style.display = permiteDoble ? '' : 'none';
            if (!tieneColor) color.checked = false;
            if (!permiteDoble) doble.checked = false;

            const n = Math.max(parseInt(cantidad.value, 10) || 0, 0);
            let total = (precio + (color.checked ? recargo : 0)) * n;
            if (doble.checked && permiteDoble) total *= (1 - DESCUENTO_DOBLE_CARA);
            salida.textContent = total.toFixed(2);
        }

        ['change', 'input'].forEach(function (ev) { caja.addEventListener(ev, calcular); });
        calcular();
    });
})();
