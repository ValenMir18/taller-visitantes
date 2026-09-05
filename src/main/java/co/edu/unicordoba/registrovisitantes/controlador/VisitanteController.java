package co.edu.unicordoba.registrovisitantes.controlador;

import co.edu.unicordoba.registrovisitantes.modelo.Visitante;
import co.edu.unicordoba.registrovisitantes.servicio.VisitanteService;
import co.edu.unicordoba.registrovisitantes.util.TextoUtil;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitantes")
public class VisitanteController {

    private final VisitanteService servicio;

    private static final Instant ARRANQUE = Instant.now();

    public VisitanteController(VisitanteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public Visitante registrar(@RequestBody Map<String, Object> body) {
        String nombre = (String) body.get("nombre");
        int edad = Integer.parseInt(body.get("edad").toString());
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

    @GetMapping("/instancia")
    public Map<String, Object> instancia() throws Exception {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("host", InetAddress.getLocalHost().getHostName());
        r.put("arranqueJvm", ARRANQUE.toString());
        r.put("creados", Visitante.getTotalCreados());
        r.put("registrados", servicio.contarRegistrados());
        return r;
    }

    @PostMapping("/fantasma")
    public Map<String, Object> fantasma() {
        new Visitante("objeto fantasma", 30);
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("registradosEnElServicio", servicio.contarRegistrados());
        resultado.put("creadosEnLaClase", Visitante.getTotalCreados());
        return resultado;
    }
}