package co.edu.unicordoba.registrovisitantes.modelo;

import co.edu.unicordoba.registrovisitantes.util.TextoUtil;

/**
 * Clase de dominio. Representa a UNA persona que se registra.
 *
 * Justificación de cada miembro (ver informe, punto 2.a):
 *
 * - id, nombre, edad -> atributos de INSTANCIA: cada Visitante tiene
 *   su propio id, su propio nombre y su propia edad. No tiene sentido
 *   que sean compartidos entre objetos.
 *
 * - totalCreados -> atributo STATIC: no pertenece a ningún Visitante
 *   en particular, sino a la clase Visitante como un todo. Debe
 *   sobrevivir aunque los objetos individuales sean descartados por el
 *   recolector de basura (ver el endpoint /fantasma).
 *
 * - EDAD_MINIMA -> constante STATIC FINAL: un único valor, compartido,
 *   de solo lectura. Igual que Math.PI.
 */
public class Visitante {

    // ---------- ESTADO DE INSTANCIA ----------
    private final int id;
    private final String nombre;
    private final int edad;

    // ---------- ESTADO DE CLASE (static) ----------
    private static int totalCreados;
    public static final int EDAD_MINIMA = 18;

    static { // corre UNA sola vez, al cargar la clase
        totalCreados = 0;
    }

    public Visitante(String nombre, int edad) {
        totalCreados++;                 // sin this: pertenece a la CLASE
        this.id = totalCreados;         // con this: pertenece a ESTE objeto
        this.nombre = TextoUtil.normalizarNombre(nombre);
        this.edad = edad;
    }

    // ---------- MÉTODOS DE INSTANCIA ----------
    // Necesitan un objeto concreto porque su resultado depende de "this".

    public boolean esMayorDeEdad() {
        return this.edad >= EDAD_MINIMA;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    // ---------- MÉTODO DE CLASE (static) ----------
    // No necesita ningún Visitante concreto: solo consulta el estado
    // compartido de la clase.

    public static int getTotalCreados() {
        return totalCreados;
    }

    @Override
    public String toString() {
        return "Visitante{id=" + id + ", nombre='" + nombre + "', edad=" + edad + '}';
    }
}
