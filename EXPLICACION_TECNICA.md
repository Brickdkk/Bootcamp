# Guía de Estudio Técnica: Sistema de Inventario

Este documento es una guía de análisis exhaustivo del código, diseñada para una exposición técnica. Se desglosa la arquitectura, las decisiones de diseño y la implementación línea por línea.

## 1. Arquitectura y Principios de Diseño

El sistema se fundamenta en una **arquitectura en capas (Layered Architecture)**, un pilar del diseño de software robusto. El objetivo es la **separación de responsabilidades (Separation of Concerns)**, donde cada capa tiene un rol único y bien definido.

*   **Capas del Sistema**:
    1.  **`app` (Presentación)**: Responsable de la interacción con el usuario (UI de consola). No contiene lógica de negocio.
    2.  **`service` (Servicio)**: Orquesta la lógica de negocio. Actúa como fachada, coordinando las operaciones entre la presentación y la persistencia.
    3.  **`domain` (Dominio)**: El corazón del software. Contiene las entidades (`Producto`, `Movimiento`) y las reglas de negocio inherentes a ellas.
    4.  **`repo` (Persistencia)**: Abstrae el acceso a los datos. Su responsabilidad es leer y escribir desde/hacia una fuente de datos (en este caso, archivos de texto).
    5.  **`util` (Utilidades)**: Clases con funcionalidades genéricas y reutilizables.

*   **Principio de Inversión de Dependencias (DIP)**:
    *   Las capas de alto nivel (`service`) no dependen de las implementaciones de bajo nivel (`repo`), sino de **abstracciones (interfaces)**.
    *   En `InventarioService`, no se instancia `new TxtProductosRepo()`. En su lugar, se recibe una `RepositorioProductos` (la interfaz) a través del constructor. Esto se conoce como **Inyección de Dependencias (DI)** y es crucial para la modularidad y la capacidad de prueba.

---

## 2. Análisis Detallado por Paquete

### Paquete `domain`

Contiene las clases que modelan el mundo del problema. Son POJOs (Plain Old Java Objects) que encapsulan datos y, en algunos casos, lógica intrínseca a esos datos.

*   **`Movimiento.java`**:
    *   **`public abstract class Movimiento`**: Se declara `abstract` porque no tiene sentido crear un "movimiento genérico". Un movimiento es siempre algo concreto: una `Compra` o una `Venta`.
    *   **`public abstract int aplicar(int stockActual)`**: Este es el núcleo del polimorfismo en el dominio. Se define un "contrato": toda clase que herede de `Movimiento` **debe** especificar cómo modifica el stock. La superclase define el **qué**, las subclases definen el **cómo**.

*   **`Compra.java` / `Venta.java`**:
    *   **`public class Compra extends Movimiento`**: Implementan la herencia. Adquieren todos los campos de `Movimiento`.
    *   **`@Override public int aplicar(...)`**: Sobrescriben el método abstracto. `Compra` implementa la suma (`+`), `Venta` implementa la resta (`-`). Esta es la materialización del polimorfismo.

*   **`TipoMovimiento.java`**:
    *   **`public enum TipoMovimiento`**: Usamos un `enum` en lugar de `String` o `int` para garantizar la **seguridad de tipos (type safety)**. Esto previene errores en tiempo de ejecución, ya que el compilador se asegura de que solo se puedan usar los valores definidos (`VENTA`, `COMPRA`).

### Paquete `repo`

Abstrae la lógica de persistencia. El resto de la aplicación no sabe (ni le importa) que los datos se guardan en archivos de texto.

*   **Interfaces (`RepositorioProductos`, `RepositorioMovimientos`)**:
    *   Definen los métodos que la capa de servicio espera. Son el contrato que cualquier implementación de persistencia debe cumplir. Si mañana migramos a una base de datos, crearíamos `SqlProductosRepo implements RepositorioProductos` y la capa de servicio funcionaría sin cambios.

*   **`TxtProductosRepo.java`**:
    *   **`private List<Producto> cache`**: Se usa una caché en memoria para minimizar las operaciones de I/O (lectura/escritura de disco), que son lentas. Los datos se leen del archivo una vez al inicio y luego todas las operaciones se realizan sobre esta lista.
    *   **`cargar()`**:
        *   `try (BufferedReader br = Files.newBufferedReader(rutaArchivo))`: Utiliza `try-with-resources`, una característica de Java que cierra automáticamente el `BufferedReader`, previniendo fugas de recursos.
        *   `linea.split("\\|", -1)`: Divide cada línea usando `|` como delimitador. El `-1` asegura que se incluyan los campos vacíos al final si los hubiera.
    *   **`guardar()`**:
        *   Esta implementación es simple pero **ineficiente para grandes volúmenes de datos**, ya que reescribe el archivo completo en cada modificación. Para un sistema de mayor escala, se usarían estrategias más optimizadas.
    *   **`actualizarOInsertar(Producto producto)`**:
        *   `buscarPorSku(...).ifPresentOrElse(...)`: Usa `Optional` para manejar de forma segura la posible ausencia de un producto. El `ifPresentOrElse` es una forma funcional y expresiva de manejar ambos casos (si existe, se actualiza; si no, se inserta).

*   **`TxtMovimientosRepo.java`**:
    *   **`agregar(Movimiento movimiento)`**:
        *   `Files.newBufferedWriter(rutaArchivo, StandardOpenOption.CREATE, StandardOpenOption.APPEND)`: A diferencia del repositorio de productos, este método usa `APPEND`. No reescribe el archivo, sino que **añade una nueva línea al final**. Esto es intencional y sigue el patrón de un **log de transacciones**, donde cada movimiento es un registro histórico inmutable.

### Paquete `service`

El orquestador de la lógica de negocio.

*   **`InventarioService.java`**:
    *   **Constructor y DI**: `public InventarioService(RepositorioProductos repoProd, ...)`: Como se mencionó, esto es inyección de dependencias manual. Permite inyectar "mocks" (repositorios falsos) en las pruebas unitarias para aislar y probar la lógica de `InventarioService` sin depender de archivos reales.
    *   **`registrarVenta(...)`**:
        1.  `Producto producto = ... .orElseThrow(() -> new RuntimeException(...))`: `Optional` es clave aquí. Previene `NullPointerException`. Si `buscarPorSku` no encuentra nada, en lugar de devolver `null`, devuelve un `Optional.empty()`. El `.orElseThrow()` permite manejar este caso de forma explícita y robusta, deteniendo la operación inmediatamente si el producto no existe (principio de "fail-fast").
        2.  **Validaciones de negocio**: El `if (!producto.estaActivo())`, `if (cantidad <= 0)`, etc., son las **reglas de negocio explícitas**. La capa de servicio es el guardián de estas reglas.
        3.  `Movimiento venta = new Venta(...)`: Aquí se instancia la clase concreta.
        4.  `producto.stock = venta.aplicar(producto.stock)`: **Invocación polimórfica**. Aunque `venta` podría ser una variable de tipo `Movimiento`, el método que se ejecuta en tiempo de ejecución es el de la clase `Venta`, gracias al enlace dinámico (dynamic binding) de Java.
        5.  `repositorioProductos.actualizarOInsertar(producto)`: Se delega la persistencia al repositorio.

### Paquete `app`

La cara visible para el usuario.

*   **`Main.java`**:
    *   **`public static void main(String[] args)`**: El punto de entrada estándar de una aplicación Java.
    *   Aquí se realiza la **composición de objetos (Object Composition)**: se crean las instancias de las dependencias (`TxtProductosRepo`, `TxtMovimientosRepo`) y se "inyectan" en la clase que las necesita (`InventarioService`).
*   **`MenuCliente.java` / `MenuAdmin.java`**:
    *   Su única responsabilidad es traducir la entrada del usuario en llamadas a métodos de `InventarioService` y mostrar los resultados.
    *   **`try { ... } catch (NumberFormatException e) { ... }`**: Manejo básico de excepciones para entradas inválidas (si el usuario escribe "hola" en lugar de un número).

### Paquete `util`

*   **`ValidadorRut.java`**:
    *   **`public static boolean esValido(...)`**: Los métodos son `static` porque la validación de un RUT es una función pura: no depende de ningún estado interno de un objeto `ValidadorRut`. Dados los mismos argumentos, siempre producirá el mismo resultado. Es una clase de utilidad clásica.

---
Este análisis te proporciona el **qué** hace el código y, más importante, el **porqué** de las decisiones de diseño. Estás listo para tu exposición.
