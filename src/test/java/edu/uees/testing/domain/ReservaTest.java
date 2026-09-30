package edu.uees.testing.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas agregadas despues de mirar la cobertura de la entidad Reserva.
 * Son pequeñas, pero cubren caminos que la suite del servicio no recorre directamente.
 */
class ReservaTest {

    @Test
    void reservaNuevaEmpiezaPendienteYConservaDatos() {
        Reserva reserva = new Reserva("R-100", "VIP");

        assertEquals("R-100", reserva.getId());
        assertEquals("VIP", reserva.getTipo());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test
    void tipoNuloUsaNormalPorDefecto() {
        // Este detalle estaba en el constructor y era facil dejarlo sin probar.
        Reserva reserva = new Reserva("R-101", null);

        assertEquals("NORMAL", reserva.getTipo());
    }

    @Test
    void cancelarCambiaElEstadoACancelada() {
        Reserva reserva = new Reserva("R-102", "NORMAL");

        reserva.cancelar();

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void idVacioNoPermiteCrearReserva() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> new Reserva("   ", "NORMAL")
        );

        assertEquals("Id obligatorio", error.getMessage());
    }

    @Test
    void idNuloNoPermiteCrearReserva() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> new Reserva(null, "NORMAL")
        );

        assertEquals("Id obligatorio", error.getMessage());
    }
}
