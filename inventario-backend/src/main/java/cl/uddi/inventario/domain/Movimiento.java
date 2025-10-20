package cl.uddi.inventario.domain;

import java.time.Instant;

public abstract class Movimiento {
    public Instant fecha = Instant.now();
    public MovementType tipo;
    public int sku;
    public int cantidad;     
    public int precioUnit;
    public String doc;
    public String nota;
    public String rutUsuario;

    protected Movimiento(MovementType tipo, int sku, int cantidad, int precioUnit,
                         String doc, String nota, String rutUsuario) {
        this.tipo = tipo; this.sku = sku; this.cantidad = cantidad; this.precioUnit = precioUnit;
        this.doc = doc; this.nota = nota; this.rutUsuario = rutUsuario;
    }

    public abstract int aplicar(int stockActual);
}

