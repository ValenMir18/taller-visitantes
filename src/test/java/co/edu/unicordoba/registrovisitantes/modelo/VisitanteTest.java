package co.edu.unicordoba.registrovisitantes.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitanteTest {

    @Test
    void elContadorStaticEsCompartido() {
        int antes = Visitante.getTotalCreados();
        new Visitante("ana", 25);
        new Visitante("luis", 40);
        assertEquals(antes + 2, Visitante.getTotalCreados());
    }

    @Test
    void instanceofYCastSeguro() {
        Object o = new Visitante("marta", 16);
        assertTrue(o instanceof Visitante);

        if (o instanceof Visitante v) { // patrón de tipo
            assertFalse(v.esMayorDeEdad());
        }

        Object texto = "Hola";
        assertFalse(texto instanceof Visitante);
    }

    @Test
    void normalizaElNombreAlCrearElVisitante() {
        Visitante v = new Visitante("  ana   maria perez ", 25);
        assertEquals("Ana Maria Perez", v.getNombre());
    }

    @Test
    void esMayorDeEdadUsaLaConstanteStatic() {
        Visitante mayor = new Visitante("pedro", 18);
        Visitante menor = new Visitante("sofia", 17);
        assertTrue(mayor.esMayorDeEdad());
        assertFalse(menor.esMayorDeEdad());
    }
}
