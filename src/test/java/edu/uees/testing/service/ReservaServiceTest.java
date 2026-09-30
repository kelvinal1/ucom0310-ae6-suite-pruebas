package edu.uees.testing.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaServiceTest {

    private final ReservaService servicio = new ReservaService(
            reserva -> true,
            reserva -> { },
            reserva -> { }
    );

    @Test
    void entornoJUnitFunciona() {
        // Esta prueba venia en el proyecto base, la dejo porque sigue siendo valida.
        assertTrue(true);
    }

    @Test
    void cincoHorasPermitenCancelar() {
        int horasAnticipacion = 5;

        boolean resultado = servicio.puedeCancelar(horasAnticipacion);

        assertTrue(resultado);
    }

    @Test
    void dosHorasExactasTodaviaPermitenCancelar() {
        // Justo en 2 horas esta el borde que me interesa probar.
        boolean resultado = servicio.puedeCancelar(2);

        assertTrue(resultado);
    }

    @Test
    void unaHoraYaNoPermiteCancelar() {
        boolean resultado = servicio.puedeCancelar(1);

        assertFalse(resultado);
    }

    @Test
    void tipoNormalMantieneElTotalSinDescuento() {
        double total = servicio.calcularTotal("NORMAL", 100.0);

        assertEquals(100.0, total, 0.001);
    }

    @Test
    void tipoVipAplicaQuincePorCientoDeDescuento() {
        double total = servicio.calcularTotal("VIP", 100.0);

        assertEquals(85.0, total, 0.001);
    }

    @Test
    void tipoEstudianteAplicaDiezPorCientoDeDescuento() {
        double total = servicio.calcularTotal("ESTUDIANTE", 100.0);

        assertEquals(90.0, total, 0.001);
    }

    @Test
    void totalCeroSeMantieneEnCero() {
        double total = servicio.calcularTotal("VIP", 0.0);

        assertEquals(0.0, total, 0.001);
    }

    @Test
    void tipoVipEnMinusculasTambienAplicaDescuento() {
        double total = servicio.calcularTotal("vip", 100.0);

        assertEquals(85.0, total, 0.001);
    }

    @Test
    void totalNegativoLanzaExcepcion() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal("NORMAL", -1.0)
        );

        assertEquals("Total base inválido", error.getMessage());
    }
}
