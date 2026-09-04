# registro-visitantes

Taller 01: *del `new` al contenedor* — Programación III (Instancia y `static`)
Universidad de Córdoba — Ing. de Sistemas · Docente: Mag. Alberto Paternina León

API REST en Spring Boot que registra visitantes y sirve para comprobar,
con código ejecutable, la diferencia entre miembros **de instancia**,
miembros **`static`** y un **bean singleton** de Spring.

## Requisitos

- Java 21
- Maven (o usar el wrapper `./mvnw` si lo agregas con `mvn -N wrapper:wrapper`)

## Ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Ejecutar las pruebas unitarias (Paso 8):

```bash
mvn test
```

## Estructura

```
src/main/java/co/edu/unicordoba/registrovisitantes
├── RegistroVisitantesApplication.java   # main() — static, arranca sin objetos
├── modelo/Visitante.java                # atributos/métodos de instancia y static
├── util/TextoUtil.java                  # utilidades puras, solo static
├── servicio/VisitanteService.java       # @Service — bean singleton
└── controlador/VisitanteController.java # @RestController — endpoints
```

## Endpoints

| Método | Ruta                          | Descripción                                   |
|--------|--------------------------------|------------------------------------------------|
| POST   | `/api/visitantes`              | Registra un visitante (`nombre`, `edad`)       |
| GET    | `/api/visitantes`               | Lista los visitantes registrados               |
| GET    | `/api/visitantes/conteos`       | Compara contador de instancia vs. contador static |
| GET    | `/api/visitantes/normalizar`    | Normaliza un texto usando `TextoUtil` (static) |
| POST   | `/api/visitantes/fantasma`      | Paso 6: crea un objeto sin guardarlo           |

### Pruebas con curl

```bash
curl -X POST "http://localhost:8080/api/visitantes?nombre=ana%20maria%20perez&edad=25"
curl -X POST "http://localhost:8080/api/visitantes?nombre=luis%20diaz&edad=40"
curl -X POST "http://localhost:8080/api/visitantes?nombre=juan%20paez&edad=16"

curl http://localhost:8080/api/visitantes
curl http://localhost:8080/api/visitantes/conteos
curl "http://localhost:8080/api/visitantes/normalizar?texto=pedro%20jose%20DIAZ"
curl -X POST http://localhost:8080/api/visitantes/fantasma
```

Salida esperada de `/conteos` con los tres registros de arriba:

```json
{ "registradosEnElServicio": 3, "creadosEnLaClase": 3, "edadMinima": 18 }
```

Tras llamar a `/fantasma` una vez:

```json
{ "registradosEnElServicio": 3, "creadosEnLaClase": 4 }
```

**Pregunta central (Paso 6):** `creadosEnLaClase` sigue subiendo porque
`totalCreados++` vive en el *constructor* de `Visitante` — corre cada
vez que se ejecuta `new Visitante(...)`, sin importar si el objeto se
guarda en algún lado. `registradosEnElServicio`, en cambio, depende de
que el objeto haya sido añadido a la lista de instancia del bean
`VisitanteService`; el visitante "fantasma" nunca llega a esa lista, así
que ese contador no se mueve. Es la diferencia entre estado de la
**clase** (compartido, automático) y estado de **un objeto concreto**
(depende de qué hagamos con la referencia).

## Reproducir los dos errores del Paso 7 (para la evidencia del informe)

Estos fragmentos se agregan **temporalmente**, se capturan y luego se
**eliminan** del código antes de la entrega final (el repositorio ya
está limpio de ellos).

**Error A — inyectar en un campo `static`.** Agregar en
`VisitanteController`:

```java
@Autowired
private static VisitanteService roto; // MAL

@GetMapping("/roto")
public String probarRoto() {
    return "roto es null: " + (roto == null);
}
```

Compila y arranca; `GET /api/visitantes/roto` responde `true` y el log
de arranque muestra:

```
Autowired annotation is not supported on static fields: private static ... VisitanteService roto
```

**Error B — usar `this` en un método `static`.** Agregar en `Visitante`:

```java
public static String saludoRoto() {
    return "Hola " + this.nombre; // no compila
}
```

`mvn compile` falla con:

```
javac: non-static variable this cannot be referenced from a static context
```

La diferencia clave para el informe: el Error A es un **fallo en
ejecución** (`null` silencioso); el Error B es un **fallo en
compilación** (el compilador lo detiene antes de arrancar).

## Idea central de la sesión

- **Instancia:** lo que es de cada objeto. Requiere `new`, dispone de `this`.
- **`static`:** lo que es de la clase. Existe sin objetos, se comparte entre todos.
- **Regla:** de instancia a `static` se puede leer; de `static` a instancia, no (no hay `this`).
- **En Spring Boot:** el contenedor sustituye al `new`, pero la distinción sigue viva —
  por eso `@Autowired` no funciona en campos `static`.
