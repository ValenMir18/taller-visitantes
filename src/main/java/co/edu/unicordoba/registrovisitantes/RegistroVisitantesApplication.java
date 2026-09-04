package co.edu.unicordoba.registrovisitantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 *
 * El método main es static: la JVM debe poder ejecutarlo sin haber
 * creado antes ningún objeto. Spring Boot arranca desde aquí y, a
 * partir de este punto, es el contenedor (no nosotros con "new") quien
 * crea y administra los beans de la aplicación (VisitanteService,
 * VisitanteController, etc.).
 */
@SpringBootApplication
public class RegistroVisitantesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistroVisitantesApplication.class, args);
    }

}
