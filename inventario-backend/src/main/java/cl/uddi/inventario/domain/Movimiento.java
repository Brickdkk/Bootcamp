package cl.uddi.inventario.domain;

import java.time.Instant;

public abstract class Movimiento {
    public Instant fecha = Instant.now();
    public TipoMovimiento tipo;
    public int sku;
    public int cantidad;
    public int precioUnitario;
    public String documento;
    public String nota;
    public String rutUsuario;

    protected Movimiento(TipoMovimiento tipo, int sku, int cantidad, int precioUnitario,
                         String documento, String nota, String rutUsuario) {
        this.tipo = tipo;
        this.sku = sku;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.documento = documento;
        this.nota = nota;
        this.rutUsuario = rutUsuario;
    }

    public abstract int aplicar(int stockActual);
}
