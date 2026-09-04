# registro-visitantes

Taller 01: *del `new` al contenedor* — Programación III (Instancia y `static`)
<<<<<<< HEAD
Taller 02: *del `localhost` a la nube* — despliegue con GitHub, Docker y Render
=======
>>>>>>> 05622a388b9a18919712147de06d6d4037373e25
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
<<<<<<< HEAD
| GET    | `/api/visitantes/instancia`     | Taller 02: host, arranque de la JVM y conteos  |
=======
>>>>>>> 05622a388b9a18919712147de06d6d4037373e25

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

<<<<<<< HEAD
## Taller 02: despliegue con Docker y Render

### Probar la imagen localmente

```bash
docker build -t registro-visitantes .
docker run --rm -p 8081:8080 registro-visitantes
curl http://localhost:8081/api/visitantes/instancia
```

Y para confirmar que el puerto dinámico funciona (como lo hará Render):

```bash
docker run --rm -e PORT=9000 -p 8082:9000 registro-visitantes
curl http://localhost:8082/api/visitantes/instancia
```

### Subir a GitHub

```bash
git init
git add .
git commit -m "Taller 02: proyecto listo para desplegar"
git branch -M main
git remote add origin https://github.com/usuario/registro-visitantes.git
git push -u origin main
```

Verificar en GitHub que la carpeta `target/` **no** quedó subida.

### Desplegar en Render

1. Crear cuenta en Render e iniciar sesión con GitHub.
2. **New +** → **Web Service** y seleccionar este repositorio.
3. En **Language** elegir **Docker** (Java no es runtime nativo de Render).
4. Región más cercana y plan **Free**.
5. **Health Check Path**: `/api/visitantes`.
6. **Deploy**.

### Probar la API pública

```bash
BASE=https://registro-visitantes.onrender.com
curl -X POST "$BASE/api/visitantes?nombre=ana%20maria%20perez&edad=25"
curl "$BASE/api/visitantes/conteos"
curl "$BASE/api/visitantes/instancia"
```

En el plan Free la primera petición del día puede tardar: el servicio se
duerme tras inactividad.

### Paso 7 — el estado no sobrevive al reinicio

1. Registrar tres visitantes en la API pública y anotar `/conteos` e `/instancia`.
2. En el panel de Render: **Manual Deploy → Restart service**.
3. Consultar de nuevo `/conteos` e `/instancia`: `creados`, `registrados`
   vuelven a `0` y `arranqueJvm` cambia.

Esto responde el ámbito real de `static`: dura mientras dure la **clase
cargada en esa JVM**, no el proceso lógico de negocio ni el contenedor
en sí. Un reinicio crea una JVM nueva, con su propia copia del estado
static desde cero.

### Paso 8 — el mismo código en dos servidores

```bash
docker run -d --name n1 -p 8091:8080 registro-visitantes
docker run -d --name n2 -p 8092:8080 registro-visitantes

curl -X POST "http://localhost:8091/api/visitantes?nombre=ana&edad=25"
curl "http://localhost:8091/api/visitantes/instancia"
curl "http://localhost:8092/api/visitantes/instancia"
```

`n1` reporta 1 y `n2` reporta 0, con `host` y `arranqueJvm` distintos:
cada contenedor tiene su propia JVM y, por tanto, su propia copia del
estado `static`. **No** es un mecanismo válido para compartir datos
entre usuarios de una aplicación web con más de una instancia; para eso
se necesita un almacén externo y común a todas las instancias (por
ejemplo, una base de datos como PostgreSQL).

### Errores a reproducir y revertir (evidencia para el informe)

**Error A — puerto fijo.** Cambiar temporalmente a `server.port=8080` y
volver a desplegar: el build es exitoso pero Render nunca detecta el
puerto (`Port scan timeout reached`), porque la app no escucha en el
puerto que Render le asignó por la variable `PORT`. Se revierte a
`server.port=${PORT:8080}`.

**Error B — secreto en el historial.** Agregar a propósito una
credencial en `application.properties`, confirmarla y subirla; luego
borrarla y confirmar de nuevo. `git log -p -- src/main/resources/application.properties | grep -i password`
muestra que el secreto **sigue** en el historial: borrarlo del archivo
no lo borra de git. Un secreto publicado se rota, no se "limpia"; la
configuración sensible va en variables de entorno de Render, nunca en
el repositorio.

=======
>>>>>>> 05622a388b9a18919712147de06d6d4037373e25
## Idea central de la sesión

- **Instancia:** lo que es de cada objeto. Requiere `new`, dispone de `this`.
- **`static`:** lo que es de la clase. Existe sin objetos, se comparte entre todos.
- **Regla:** de instancia a `static` se puede leer; de `static` a instancia, no (no hay `this`).
- **En Spring Boot:** el contenedor sustituye al `new`, pero la distinción sigue viva —
  por eso `@Autowired` no funciona en campos `static`.
