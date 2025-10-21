# Cómo Funciona Nuestro Programa de Inventario (Explicado Fácil)

¡Hola! Aquí te explico cómo está hecho nuestro programa. Imagina que es como una caja de LEGO, donde cada pieza tiene una función y se conecta con las demás para que todo funcione.

## ¿Cómo está Organizado el Código?

Para no tener un desorden, guardamos el código en "cajones" (que en Java se llaman **paquetes**). Cada cajón tiene archivos para una tarea específica:

1.  **Paquete `app` (La parte que se ve)**:
    *   Aquí está el código que habla con nosotros, el usuario. Es la consola donde escribimos las opciones del menú.

2.  **Paquete `service` (El cerebro del programa)**:
    *   Contiene la clase `InventarioService`, que es como el cerebro. Sabe qué hacer cuando elegimos una opción, como "vender un producto" o "agregar uno nuevo".

3.  **Paquete `domain` (Los moldes de nuestros datos)**:
    *   Aquí guardamos los "moldes" (las clases) para los objetos que usamos. Por ejemplo, tenemos un molde `Producto` para crear todos los productos de la tienda.

4.  **Paquete `repo` (Los que guardan y leen los archivos)**:
    *   Estos son los encargados de hablar con los archivos `.txt`. Saben cómo leer la lista de productos y cómo guardar un nuevo movimiento.

5.  **Paquete `util` (Herramientas útiles)**:
    *   Aquí ponemos código que nos ayuda en varias partes, como la herramienta para saber si un RUT es válido.

---

## Un Vistazo a las Clases Más Importantes

*   **`Producto.java`**: Es el molde para crear cualquier producto. Dice que cada producto debe tener un `sku`, un `nombre`, un `precio`, `stock`, etc.

*   **`Movimiento.java`**: Es como un "molde general" para cualquier cosa que cambie el stock. Dice que toda operación (como una compra o una venta) debe tener una `fecha`, una `cantidad`, un `sku`, etc.
    *   Tiene una tarea llamada `aplicar()`, pero no explica cómo hacerla. Solo dice que **hay que hacerla**.

*   **`Compra.java` y `Venta.java`**: Son moldes más específicos. Son como "hijos" del molde `Movimiento`.
    *   Saben hacer todo lo que un `Movimiento` hace.
    *   Pero cada uno hace la tarea `aplicar()` a su manera:
        *   `Compra` suma la cantidad al stock.
        *   `Venta` resta la cantidad al stock.

*   **`InventarioService.java`**: Es el director de orquesta. Cuando queremos vender algo, le avisamos a esta clase. Ella se encarga de:
    1.  Verificar que el producto exista y que haya stock.
    2.  Crear un objeto `Venta`.
    3.  Pedirle a ese objeto que reste el stock.
    4.  Avisarle al "encargado de archivos" que guarde los cambios.

---

## ¿Y esas palabras raras de Programación?

Aquí te explico de forma sencilla los conceptos que usamos:

### Herencia (Como una familia)

Esto es como en una familia, donde los hijos heredan cosas de los padres.

*   **Ejemplo**: `Compra` y `Venta` son **hijos** de `Movimiento`.
*   Esto significa que no tuvimos que escribirles `fecha`, `sku`, `cantidad`, etc., porque ya lo heredaron de su **padre**, `Movimiento`. ¡Nos ahorra mucho trabajo!

### Polimorfismo (Una palabra difícil para una idea fácil)

Significa "muchas formas". En nuestro código, lo usamos para tratar a `Compra` y `Venta` como si fueran lo mismo: un `Movimiento`.

*   **Ejemplo**: En `InventarioService`, cuando creamos una venta, la guardamos en una variable de tipo `Movimiento`.

    ```java
    // La variable es de tipo Movimiento, pero el objeto que contiene es una Venta
    Movimiento miMovimiento = new Venta(...);
    ```
*   Lo genial es que cuando usamos `miMovimiento.aplicar()`, ¡Java sabe que tiene que llamar al método de `Venta` (el que resta)! Si en la variable hubiéramos guardado una `Compra`, Java habría sabido que tenía que sumar. Es como si la variable se adaptara al objeto que tiene dentro.

### Colaboración (Clases que se ayudan)

Es simplemente cómo las clases trabajan juntas. Ninguna clase hace todo sola, se piden ayuda entre ellas.

*   **Ejemplo**: `InventarioService` no sabe cómo guardar cosas en un archivo. ¡Y no le importa!
*   Simplemente le pide ayuda a un objeto de tipo `RepositorioProductos` y le dice: "Oye, guarda este producto". El repositorio se encarga del resto.
*   Esto es genial porque si el día de mañana queremos guardar en una base de datos en lugar de un archivo, solo cambiamos la clase `Repositorio` y el `InventarioService` seguirá funcionando igual, sin enterarse del cambio.
