package co.edu.unicordoba.registrovisitantes.controlador;

import co.edu.unicordoba.registrovisitantes.modelo.Visitante;
import co.edu.unicordoba.registrovisitantes.servicio.VisitanteService;
import co.edu.unicordoba.registrovisitantes.util.TextoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitantes")
public class VisitanteController {

    private final VisitanteService servicio;

    // Inyección por CONSTRUCTOR: NO es static. Spring crea este
    // controlador una sola vez y le entrega, por parámetro, el bean
    // VisitanteService que ya tiene administrado.
    public VisitanteController(VisitanteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public Visitante registrar(@RequestParam String nombre, @RequestParam int edad) {
        return servicio.registrar(nombre, edad);
    }

    @GetMapping
    public List<Visitante> listar() {
        return servicio.listar();
    }

    @GetMapping("/conteos")
    public Map<String, Object> conteos() {
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("registradosEnElServicio", servicio.contarRegistrados());
        resultado.put("creadosEnLaClase", servicio.contarCreadosEnLaClase());
        resultado.put("edadMinima", Visitante.EDAD_MINIMA);
        return resultado;
    }

    @GetMapping("/normalizar")
    public Map<String, String> normalizar(@RequestParam String texto) {
        return Map.of("normalizado", TextoUtil.normalizarNombre(texto)); // static
    }

    /**
     * Paso 6: el experimento que revela la diferencia.
     * Crea un Visitante y NO lo guarda en el servicio (no llega a la
     * lista de instancia), pero SÍ incrementa el contador static de la
     * clase, porque ese incremento ocurre dentro del constructor,
     * fuera de nuestro control sobre "quién lo guarda después".
     */
    @PostMapping("/fantasma")
    public Map<String, Object> fantasma() {
        new Visitante("objeto fantasma", 30);
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("registradosEnElServicio", servicio.contarRegistrados());
        resultado.put("creadosEnLaClase", Visitante.getTotalCreados());
        return resultado;
    }
}
