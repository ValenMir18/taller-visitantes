package co.edu.unicordoba.registrovisitantes.util;

/**
 * Utilidades puras de texto.
 *
 * Todos sus métodos son static por la misma razón que Math.pow():
 * el resultado depende solo de los parámetros recibidos, nunca del
 * estado de "un objeto TextoUtil". No existe tal cosa como "una
 * instancia de utilidades", así que la clase ni siquiera debe
 * instanciarse (constructor privado que lanza excepción).
 */
public final class TextoUtil {

    private TextoUtil() { // nunca se instancia
        throw new UnsupportedOperationException("Clase de utilidades: no se debe instanciar");
    }

    /**
     * Convierte "  ana  MARIA perez " en "Ana Maria Perez".
     */
    public static String normalizarNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return "SIN NOMBRE";
        }
        String[] palabras = texto.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String palabra : palabras) {
            sb.append(Character.toUpperCase(palabra.charAt(0)))
              .append(palabra.substring(1))
              .append(" ");
        }
        return sb.toString().trim();
    }
}
