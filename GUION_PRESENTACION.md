# Guion para la Presentación Técnica: Sistema de Inventario

---

### **Diapositiva 1: Título**

*   **Tú dices:** "Buenos días a todos. Hoy voy a presentarles un sistema de gestión de inventario desarrollado en Java. Vamos a explorar su arquitectura, cómo está organizado el código y las decisiones de diseño que se tomaron para hacerlo robusto y mantenible."

---

### **Diapositiva 2: Arquitectura del Sistema**

*(Muestra un diagrama simple con las 5 capas: App, Service, Domain, Repo, Util)*

*   **Tú dices:** "Para empezar, la base de todo el proyecto es una **arquitectura en capas**. Esto significa que separamos el código en bloques con responsabilidades claras, como si fueran los departamentos de una empresa."

*   **Tú dices:** "Tenemos 5 capas principales:"
    1.  "**App (Presentación)**: Es la cara del programa, la consola con la que interactúa el usuario."
    2.  "**Service (Servicio)**: Es el cerebro. Coordina todo el trabajo."
    3.  "**Domain (Dominio)**: Es el corazón. Aquí viven las reglas y los datos más importantes, como qué es un 'Producto' o un 'Movimiento'."
    4.  "**Repo (Repositorio/Persistencia)**: Es la memoria a largo plazo. Se encarga de guardar y leer los datos de los archivos de texto."
    5.  "**Util (Utilidades)**: Una caja de herramientas con funciones que usamos en varias partes, como el validador de RUT."

*   **Tú dices:** "Lo más importante de este diseño es que las capas de arriba no dependen de cómo funcionan las de abajo, solo de sus **interfaces**, que son como contratos. Esto se llama **Inversión de Dependencias** y nos permite, por ejemplo, cambiar el sistema de guardado de archivos a una base de datos sin tener que modificar el resto del programa."

---

### **Diapositiva 3: El Dominio - El Corazón del Negocio**

*(Muestra el código de `Movimiento.java` y al lado `Compra.java` y `Venta.java`)*

*   **Tú dices:** "Vamos a sumergirnos en el código, empezando por el paquete de dominio. Aquí tenemos la clase `Movimiento`."

*   **Tú dices:** "Esta clase es **abstracta**. ¿Por qué? Porque un 'movimiento' por sí solo no existe; siempre es algo concreto, como una compra o una venta. Por eso, esta clase define lo que todos los movimientos tienen en común: una fecha, un SKU, una cantidad..."

*   **Tú dices:** "...y define un método clave: `aplicar()`, que también es **abstracto**. `Movimiento` obliga a sus clases hijas a que cada una defina cómo va a afectar al stock. Esto nos lleva directamente a la **Herencia**."

*   **Tú dices:** "Las clases `Compra` y `Venta` **heredan** de `Movimiento`. Toman todo lo que `Movimiento` ya define y simplemente añaden su lógica particular: `Compra` suma al stock y `Venta` resta. Esto nos ahorra reescribir código y mantiene todo muy organizado."

---

### **Diapositiva 4: La Capa de Servicio - El Orquestador**

*(Muestra el código del método `registrarVenta` en `InventarioService.java`)*

*   **Tú dices:** "Ahora, veamos cómo funciona todo junto en la capa de servicio. `InventarioService` es el director de orquesta. Miremos el método `registrarVenta` como ejemplo."

*   **Tú dices (línea por línea):**
    1.  "Primero, `Producto producto = repositorioProductos.buscarPorSku(sku).orElseThrow(...)`. Aquí, buscamos el producto. Fíjense que usamos `Optional`. Esto es una práctica moderna en Java para evitar los temidos `NullPointerException`. Si el producto no se encuentra, `orElseThrow` detiene la operación de inmediato, haciendo el sistema muy seguro."
    2.  "Luego vienen las **validaciones de negocio**: `if (!producto.estaActivo())`, `if (producto.stock < cantidad)`. El servicio actúa como un guardia, asegurándose de que solo se realicen operaciones válidas."
    3.  "Después, `Movimiento venta = new Venta(...)`. Aquí vemos el **Polimorfismo** en acción. Creamos un objeto `Venta`, pero lo guardamos en una variable de tipo `Movimiento`. Podemos hacer esto porque `Venta` es un 'hijo' de `Movimiento`."
    4.  "La magia ocurre aquí: `producto.stock = venta.aplicar(producto.stock)`. Aunque la variable `venta` es de tipo `Movimiento`, Java sabe que el objeto real es una `Venta`, por lo que ejecuta el método `aplicar` que resta el stock. Si fuera una compra, llamaría al que suma. Esto hace el código súper flexible."
    5.  "Finalmente, `repositorioProductos.actualizarOInsertar(producto)` y `repositorioMovimientos.agregar(venta)`. El servicio **delega** la tarea de guardar a los repositorios. No sabe cómo lo hacen, solo confía en que lo harán."

---

### **Diapositiva 5: La Persistencia - Abstracción de Datos**

*(Muestra el código de la interfaz `RepositorioProductos` y un trozo de `TxtProductosRepo.java`)*

*   **Tú dices:** "Hablemos de cómo guardamos los datos. Para esto, usamos el **patrón Repositorio**. La idea es crear una capa de abstracción entre la lógica de negocio y el almacenamiento de datos."

*   **Tú dices:** "Primero, definimos una **interfaz**, `RepositorioProductos`, que es como un contrato. Dice: 'Quien quiera que guarde productos, debe saber cómo `listar`, `buscarPorSku`, etc.'."

*   **Tú dices:** "Luego, tenemos `TxtProductosRepo`, que **implementa** esa interfaz. Esta clase es la que realmente trabaja con los archivos de texto. Usa una caché en memoria para ser más rápido y lee/escribe los datos separados por `|`."

*   **Tú dices:** "En `TxtMovimientosRepo`, hay un detalle interesante. Para agregar un movimiento, usamos `StandardOpenOption.APPEND`. Esto no reescribe el archivo, sino que añade la nueva línea al final, creando un historial de todas las operaciones, como un libro de contabilidad."

---

### **Diapositiva 6: Conclusiones**

*   **Tú dices:** "En resumen, este sistema, aunque sencillo, está construido sobre principios de diseño de software muy sólidos:"
    *   "**Arquitectura en Capas** para mantener el orden."
    *   **Inyección de Dependencias** e **Interfaces** para que sea flexible y fácil de probar."
    *   **Herencia y Polimorfismo** para reutilizar código y manejar diferentes tipos de operaciones de forma elegante."

*   **Tú dices:** "Este enfoque no solo hace que el código funcione, sino que también lo prepara para crecer en el futuro. Muchas gracias. ¿Alguna pregunta?"
