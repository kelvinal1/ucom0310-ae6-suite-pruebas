package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Suite principal del servicio de reservas.
 *
 * La idea aqui es probar reglas visibles del servicio y no los detalles internos.
 * Para confirmar si uso Mockito porque ahi si hay dependencias externas al flujo.
 */
@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private DisponibilidadClient disponibilidad;

    @Mock
    private ReservaRepository repository;

    @Mock
    private Notificador notificador;

    private ReservaService servicio;

    @BeforeEach
    void prepararServicio() {
        servicio = new ReservaService(disponibilidad, repository, notificador);
    }

    @Test
    void entornoJUnitFunciona() {
        // Esta prueba venia en el proyecto base, la dejo porque sigue siendo valida.
        assertTrue(true);
    }

    @Test
    void cincoHorasPermitenCancelar() {
        // Arrange: uso un valor normal, lejos del borde.
        int horasAnticipacion = 5;

        // Act
        boolean resultado = servicio.puedeCancelar(horasAnticipacion);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void dosHorasExactasTodaviaPermitenCancelar() {
        // Este es el borde importante: con 2 horas aun debe dejar cancelar.
        int horasAnticipacion = 2;

        boolean resultado = servicio.puedeCancelar(horasAnticipacion);

        assertTrue(resultado);
    }

    @Test
    void unaHoraYaNoPermiteCancelar() {
        int horasAnticipacion = 1;

        boolean resultado = servicio.puedeCancelar(horasAnticipacion);

        assertFalse(resultado);
    }

    @Test
    void tipoNormalMantieneElTotalSinDescuento() {
        double totalBase = 100.0;

        double total = servicio.calcularTotal("NORMAL", totalBase);

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
        // Quise dejar el cero porque es el limite valido antes de los negativos.
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

    @Test
    void reservaDisponibleSeConfirmaGuardaYNotifica() {
        // Arrange: aca disponibilidad funciona como Stub para controlar el escenario.
        Reserva reserva = new Reserva("R-001", "NORMAL");
        when(disponibilidad.estaDisponible(reserva)).thenReturn(true);

        // Act
        servicio.confirmar(reserva);

        // Assert: aparte del estado, me interesa que si guarde y notifique.
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        verify(disponibilidad).estaDisponible(reserva);
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    @Test
    void reservaNoDisponibleNoSeGuardaNiSeNotifica() {
        Reserva reserva = new Reserva("R-002", "NORMAL");
        when(disponibilidad.estaDisponible(reserva)).thenReturn(false);

        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> servicio.confirmar(reserva)
        );

        assertEquals("Horario no disponible", error.getMessage());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        verify(disponibilidad).estaDisponible(reserva);
        verify(repository, never()).guardar(reserva);
        verify(notificador, never()).enviarConfirmacion(reserva);
    }

    @Test
    void reservaNulaFallaAntesDeConsultarDependencias() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.confirmar(null)
        );

        assertEquals("Reserva obligatoria", error.getMessage());

        // Si la reserva ni existe, no deberiamos tocar nada externo.
        verifyNoInteractions(disponibilidad, repository, notificador);
    }
}
