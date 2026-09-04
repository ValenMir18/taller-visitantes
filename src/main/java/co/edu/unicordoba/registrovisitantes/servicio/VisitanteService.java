package co.edu.unicordoba.registrovisitantes.servicio;

import co.edu.unicordoba.registrovisitantes.modelo.Visitante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Service marca esta clase como un bean gestionado por Spring.
 *
 * Por defecto, Spring crea UNA sola instancia de VisitanteService para
 * toda la aplicación (singleton) y la reutiliza en cada petición HTTP.
 * Se parece a "static" en que ambos dan una única copia compartida,
 * pero NO es lo mismo:
 *   - "static" es una propiedad del lenguaje Java: la copia vive en la
 *     clase y existe sin que nadie la pida.
 *   - "singleton de Spring" es una decisión del contenedor: la
 *     instancia se crea con "new" (por Spring, no por nosotros) y se
 *     guarda en el contenedor de IoC para entregarla siempre que se
 *     necesite. Es un objeto normal, con "this", que sencillamente
 *     nunca se vuelve a crear.
 */
@Service
public class VisitanteService {

    // Atributo de INSTANCIA del bean: como el bean es único, esta
    // lista termina siendo, en la práctica, "compartida" por todas
    // las peticiones -- pero conceptualmente sigue siendo un campo de
    // instancia, no static.
    private final List<Visitante> registrados = new ArrayList<>();

    public Visitante registrar(String nombre, int edad) {
        Visitante visitante = new Visitante(nombre, edad);
        registrados.add(visitante);
        return visitante;
    }

    public List<Visitante> listar() {
        return List.copyOf(registrados); // copia inmutable
    }

    public int contarRegistrados() {
        return registrados.size(); // cuenta lo que EL SERVICIO guarda
    }

    public int contarCreadosEnLaClase() {
        return Visitante.getTotalCreados(); // cuenta lo que la CLASE sabe
    }
}
