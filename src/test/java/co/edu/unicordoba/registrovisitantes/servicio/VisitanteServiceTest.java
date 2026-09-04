package co.edu.unicordoba.registrovisitantes.servicio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VisitanteServiceTest {

    @Test
    void registrarAgregaAlaListaDeInstanciaDelServicio() {
        VisitanteService servicio = new VisitanteService();
        servicio.registrar("carlos", 22);
        servicio.registrar("laura", 30);

        assertEquals(2, servicio.contarRegistrados());
    }

    @Test
    void listarDevuelveCopiaInmutable() {
        VisitanteService servicio = new VisitanteService();
        servicio.registrar("carlos", 22);

        assertEquals(1, servicio.listar().size());
        assertThrowsUnsupported(() -> servicio.listar().add(null));
    }

    private void assertThrowsUnsupported(Runnable accion) {
        try {
            accion.run();
            throw new AssertionError("Se esperaba UnsupportedOperationException");
        } catch (UnsupportedOperationException esperado) {
            // correcto: la lista es inmutable
        }
    }
}
